package gui.graphic.drawables;

import gui.graphic.Drawable;
import gui.graphic.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Connection;
import representation.ConnectionEnd;
import representation.Node;
import representation.Port;
import representation.nodes.FlowNode;
import representation.nodes.SwitchNode;

public class FlowDraw implements Drawable {
	private Map<String, Point> positions = new HashMap<String, Point>();
	private Map<String, Point> inPortPositions = new HashMap<String, Point>();
	private Map<String, Point> outPortPositions = new HashMap<String, Point>();
	private int x;
	private int y;
	private int width;
	private int height;
	private int portSize;
	private int opSize;
	private int opPortSize;
	private FlowNode flow;
	private List<OpDraw> opDraws = new ArrayList<OpDraw>();
	private List<PortDraw> portDraws = new ArrayList<PortDraw>();
	private List<ConDraw> condraws = new ArrayList<ConDraw>();
	private List<PortDraw> outportDraws = new ArrayList<PortDraw>();
	private List<Drawable> selectable = new ArrayList<Drawable>();
	private Drawable selected;
	private boolean hasSelection;
	private Shape representation;
	private Shape handler;
	private Shape resizer;

	private int opOffsetX;
	private int opOffsetY;
	private int inPortOffsetX;
	private int inPortOffsetY;
	private int outPortOffsetX;
	private int outPortOffsetY;
	private int px;
	private int py;
	private int opx;
	private int opy;
	private String name;
	private boolean resize;

	public FlowDraw(int x, int y, FlowNode flow) {
		this.x = x;
		this.y = y;
		reset();
		this.setFlow(flow);
	}

	private void reset() {
		this.portSize = 40;
		this.opSize = 60;
		this.opPortSize = 10;
		this.opOffsetX = 200;
		this.opOffsetY = 50;
		this.inPortOffsetX = 40;
		this.inPortOffsetY = 40;
		this.outPortOffsetX = opOffsetX + 40;
		this.outPortOffsetY = 40;
		opDraws.clear();
		portDraws.clear();
		condraws.clear();
		outportDraws.clear();
		selectable.clear();
	}

	public void setFlow(FlowNode flow) {
		this.flow = flow;
		this.name = flow.getId();
		reset();
		Map<String, Node> nodes = flow.getNodes();
		Set<String> keySet = nodes.keySet();
		List<String> keyList = new ArrayList<String>(keySet);
		java.util.Collections.sort(keyList);

		// initialize input ports
		Collection<Port> inports = flow.getInPorts().values();
		for (Port p : inports) {
			Point pos = this.inPortPositions.get(p.getName());
			if (pos != null) {
				this.portDraws.add(new PortDraw(pos.getX(), pos.getY(),
						portSize, portSize, p.getName(), true));
			} else {
				this.portDraws
						.add(new PortDraw(x + inPortOffsetX, y + inPortOffsetY,
								portSize, portSize, p.getName(), true));
				inPortOffsetY += portSize + 30;
			}
		}

		// initialize operators
		int size = keyList.size();
		for (int i = 0; i < size; i++) {
			Node n = nodes.get(keyList.get(i));
			Point pos = this.positions.get(n.getId());
			if (pos == null) {
				if (n instanceof SwitchNode) {
					SwitchNode sn = (SwitchNode) n;
					this.opDraws.add(new SwitchDraw(x + opOffsetX, y
							+ opOffsetY, opSize, opSize, sn));
				} else {
					this.opDraws.add(new OpDraw(x + opOffsetX, y + opOffsetY,
							opSize, opSize, n));
				}
				// make way for the next operator
				opOffsetX += opSize + 50;
				opOffsetY += opSize + 40;
			} else {
				if (n instanceof SwitchNode) {
					SwitchNode sn = (SwitchNode) n;
					this.opDraws.add(new SwitchDraw(pos.getX(), pos.getY(),
							opSize, opSize, sn));
				} else {
					this.opDraws.add(new OpDraw(pos.getX(), pos.getY(), opSize,
							opSize, n));
				}
			}
		}

		// initialize output ports
		Collection<Port> outports = flow.getOutPorts().values();
		outPortOffsetX = opOffsetX + 50;
		for (Port p : outports) {
			Point pos = this.outPortPositions.get(p.getName());
			if (pos == null) {
				this.outportDraws.add(new PortDraw(x + outPortOffsetX, y
						+ outPortOffsetY, portSize, portSize, p.getName(),
						false));
				outPortOffsetY += portSize + 30;
			} else {
				this.portDraws.add(new PortDraw(pos.getX(), pos.getY(),
						portSize, portSize, p.getName(), false));
			}
		}

		// determine the flow's dimensions
		this.width = outPortOffsetX + portSize + 30;
		this.height = inPortOffsetY;
		if (outPortOffsetY > this.height)
			this.height = outPortOffsetY;
		if (opOffsetY > this.height)
			this.height = opOffsetY;
		this.height += 40;
		this.representation = new RoundRectangle2D.Double(x, y, width, height,
				10, 10);
		this.handler = new RoundRectangle2D.Double(x, y, 20, 20, 6, 6);
		this.resizer = new RoundRectangle2D.Double(x + width - 20, y + height
				- 20, 20, 20, 6, 6);

		// initialize connection draws
		createConDraws();

		selectable = new ArrayList<Drawable>();
		selectable.addAll(portDraws);
		selectable.addAll(outportDraws);
		selectable.addAll(opDraws);
	}

	public void createConDraws() {
		// initialize connections
		Map<String, Connection> connections = flow.getConnections();
		px = x + 40;
		py = y + this.inPortOffsetY + 30;
		opx = x + 400;
		opy = y + this.outPortOffsetY + 30;
		Collection<Connection> conSet = connections.values();
		condraws = new ArrayList<ConDraw>();
		for (Connection c : conSet) {
			String signame = c.getConnectionId();
			ConnectionEnd origin = c.getOrigin();

			Point op = getPointOf(origin);
			List<Point> destpoints = new ArrayList<Point>();
			List<ConnectionEnd> destinations = c.getDestinations();
			int i = 0;
			for (ConnectionEnd ce : destinations) {
				Point p = null;
				// if the operator is null, this end of the connection is a flow
				// output port
				if (ce.getNodeId() == null) {
					for (PortDraw pd : outportDraws) {
						if (pd.getName().equals(ce.getNodePortId())) {
							p = pd.getLinkPoint();
						}
					}
				} else {
					for (OpDraw od : opDraws) {
						if (od.getName().equals(ce.getNodeId())) {
							List<InternalPortDraw> ports = od.getPorts();
							for (InternalPortDraw ipd : ports) {
								if (ipd.getName().equals(ce.getNodePortId()))
									p = ipd.getLinkPoint();
							}
						}
					}
					if (p == null) {
						p = new Point(opx, opy);
						opy += 50;
					}
				}

				if (p != null)
					destpoints.add(p);
				i++;
			}
			condraws.add(new ConDraw(signame, op, destpoints));
		}
	}

	private Point getPointOf(ConnectionEnd ce) {
		Point point = null;
		if (ce == null) {
			point = new Point(px, py);
			py += 20;
		} else {
			String opId = ce.getNodeId();
			String port = ce.getNodePortId();
			// if there is no operator, this must be a flow's input port
			if (opId == null) {
				if (port != null) {
					// search the port by name and get its connection point
					for (PortDraw p : portDraws) {
						if (p.getName().equals(port)) {
							point = p.getLinkPoint();// new
						}
					}
				}
				if (point == null) {
					point = new Point(px, py);
					py += 20;
				}
				// if the operator is not null, we are looking for the operator
				// to
				// which this links
			} else {
				// System.out.println("op nenul");
				for (OpDraw od : opDraws) {
					// System.out.println("**********:" + od.toString());
					if (od.getName().equals(opId)) {
						List<InternalPortDraw> inprts = od.getPorts();
						for (InternalPortDraw pd : inprts) {
							if (pd.getName().equals(port)) {
								point = pd.getLinkPoint();
							}
						}
					}
				}
				if (point == null) {
					point = new Point(px, py);
					py += 20;
				}
			}
		}
		return point;
	}

	public Drawable selectFromPoint(Point2D p) {
		this.resize = this.resizer.contains(p);
		if (this.handler.contains(p)) {
			this.selected = this;
			this.hasSelection = true;
		} else {
			for (Drawable d : this.selectable) {
				if (d.contains(p)) {
					this.selected = d;
					this.hasSelection = true;
					return selected;
				}
			}
			selected = null;
			hasSelection = false;
		}
		return selected;
	}

	@Override
	public void draw(Graphics2D engine) {
		// draw operators
		if (this.opDraws != null && !this.opDraws.isEmpty()) {
			for (OpDraw op : opDraws)
				op.draw(engine);
		}
		// draw in ports
		if (this.portDraws != null && !this.portDraws.isEmpty()) {
			for (PortDraw p : portDraws)
				p.draw(engine);
		}
		// draw out ports
		if (this.outportDraws != null && !this.outportDraws.isEmpty()) {
			for (PortDraw p : outportDraws)
				p.draw(engine);
		}
		// draw connections
		if (this.condraws != null && !this.condraws.isEmpty())
			for (ConDraw cd : condraws)
				cd.draw(engine);
		// draw the flow's name and border
		String title = "Flow: " + this.name;
		engine.setPaint(Color.BLUE);
		engine.drawString(title, x + 30, y + 15);
		// engine.draw(representation);
		engine.draw(handler);
		// engine.draw(resizer);
	}

	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return new Point(x + width / 2, y + height / 2);
	}

	@Override
	public Point getLinkPoint() {
		// TODO Auto-generated method stub
		return new Point(x, y + height / 2);
	}

	@Override
	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		// if (this.representation != null)
		// return this.representation.contains(p);
		// return false;
		return true;
	}

	@Override
	public void setPosition(Point2D p) {
		if (this.selected == this) {
			this.x = (int) p.getX();
			this.y = (int) p.getY();
			if (this.flow != null)
				move(1, 1);
		} else if (this.resizer.contains(p)) {
			// this.re
		} else if (this.selected != null) {
			selected.setPosition(p);
			createConDraws();
		}
		// if (flow != null)

	}

	@Override
	public void move(int dx, int dy) {
		if (this.resize) {
			this.width += dx;
			this.height += dy;
		} else {
			if (this.selected == this) {
				this.x += dx;
				this.y += dy;
				if (this.flow != null)
					for (Drawable d : selectable)
						d.move(dx, dy);
			} else if (this.selected != null) {
				selected.move(dx, dy);
				if (selected instanceof PortDraw) {
					PortDraw pd = (PortDraw) selected;
					if (pd.isInput()) {
						this.inPortPositions.put(pd.getName(), pd.getOrigin());
					} else {
						this.outPortPositions.put(pd.getName(), pd.getOrigin());
					}
				} else {
					this.positions
							.put(selected.getName(), selected.getOrigin());
				}
			}
			createConDraws();
		}
		this.representation = new RoundRectangle2D.Double(x, y, width, height,
				10, 10);
		this.handler = new RoundRectangle2D.Double(x, y, 20, 20, 6, 6);
		this.resizer = new RoundRectangle2D.Double(x + width - 20, y + height
				- 20, 20, 20, 6, 6);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public Point getOrigin() {
		// TODO Auto-generated method stub
		return new Point(x, y);
	}

}
