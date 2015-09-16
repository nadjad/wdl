package gui.graphic.drawables;

import gui.graphic.Drawable;
import gui.graphic.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import representation.CaseItem;
import representation.nodes.SwitchNode;

public class SwitchDraw extends OpDraw implements Drawable {

	private int oX;
	private int oY;
	private int sizeX;
	private int sizeY;
	private int portLength = 13;
	private int portHeight = 13;
	private SwitchNode node;
	private List<InternalPortDraw> inPorts;
	private List<InternalPortDraw> swPorts;
	private InternalPortDraw condition;
	private Shape representation;
	private String text;

	private int inHeight;
	private int outH;
	private int inStartH;
	private int outStartH;
	private int inPortX;
	private int outPortX;

	public SwitchDraw(int oX, int oY, int sizeX, int sizeY, SwitchNode node) {
		super();
		this.oX = oX;
		this.oY = oY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.node = node;
		this.text = node.getId();
		initialize();
	}

	private void initialize() {
		this.representation = new Rectangle2D.Double(oX, oY, sizeX, sizeY);
		this.inPorts = new ArrayList<InternalPortDraw>();
		this.swPorts = new ArrayList<InternalPortDraw>();
		List<String> ip = node.getInList();
		List<CaseItem> sp = node.getSwCases();

		inHeight = ip.size() * (portHeight + 5);
		outH = sp.size() * (inHeight + 5);
		int midH = oY + sizeY / 2;
		inStartH = midH - inHeight / 2;
		outStartH = midH - outH / 2;
		inPortX = (int) (oX - portLength * 1.6 - portLength / 2 + portLength);
		outPortX = (int) (oX + sizeX * 1.2);

		// add the input ports
		int i = 0;
		for (String in : ip) {
			this.inPorts.add(new InternalPortDraw(in, (int) (oX - 20), inStartH
					+ i * (portHeight + 5) + 5 / 2, portLength, portHeight,
					true));
			i++;
		}
		// add the condition connection
		String name = node.getConditionConnection().getConnectionId();
		condition = new InternalPortDraw(node.getConditionConnection()
				.getConnectionId(), oX + sizeX / 2 + 15 - (name.length() * 7)
				/ 2, (int) (oY - sizeY * 0.7 / 2 - portHeight / 2), portLength,
				portHeight, true);
		// add the switch output ports
		i = 0;
		for (CaseItem ci : sp) {
			List<String> pts = ci.getPorts();
			int j = 0;
			for (String p : pts) {
				this.swPorts.add(new InternalPortDraw(p,
						(int) (oX + sizeX + 20), outStartH + i
								* (ip.size() * (portHeight + 5))
								+ (portHeight + 5) / 2 + j * (portHeight + 5),
						portLength, portHeight, false));
				j++;
			}
			// break;
			i++;
		}
	}

	@Override
	public void draw(Graphics2D engine) {
		engine.setPaint(Color.green);
		engine.rotate(Math.PI / 4, oX + sizeX / 2, oY + sizeY / 2);
		engine.fill(representation);
		engine.setPaint(Color.blue);
		engine.draw(representation);
		engine.rotate(-Math.PI / 4, oX + sizeX / 2, oY + sizeY / 2);

		int length = engine.getFontMetrics().stringWidth(this.text);
		int height = engine.getFontMetrics().getHeight();

		int posX = oX + sizeX / 2 - length / 2;
		int posY = oY + sizeY / 2 + height / 3; // + height / 2*/;
		engine.drawString(text, posX, posY);

		// deal with the ports
		engine.setPaint(Color.BLACK);

		// draw input port bar
		engine.drawLine(inPortX - 5, inStartH, inPortX - 5, inStartH + inHeight);
		engine.drawLine(inPortX - 5, inStartH + inHeight / 2, inPortX + 3,
				inStartH + inHeight / 2);
		// draw the input ports
		for (InternalPortDraw pd : inPorts) {
			pd.draw(engine);
		}
		// draw the condition por
		this.condition.draw(engine);
		// draw output port bar
		engine.drawLine(outPortX + 8, outStartH, outPortX + 8, outStartH + outH);
		engine.drawLine(outPortX + 8, outStartH + outH / 2, outPortX, outStartH
				+ outH / 2);
		// draw the output ports
		for (InternalPortDraw pod : swPorts) {
			pod.draw(engine);
		}
	}

	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getLinkPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.text;
	}

	@Override
	public List<InternalPortDraw> getPorts() {
		// TODO Auto-generated method stub
		ArrayList<InternalPortDraw> pts = new ArrayList<InternalPortDraw>();
		pts.addAll(inPorts);
		pts.add(condition);
		pts.addAll(swPorts);
		return pts;
	}

	@Override
	public boolean contains(Point2D p) {
		return this.representation.contains(p);
	}

	@Override
	public void move(int dx, int dy) {
		this.oX += dx;
		this.oY += dy;
		initialize();
	}
}
