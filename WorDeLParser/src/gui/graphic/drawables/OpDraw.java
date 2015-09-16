package gui.graphic.drawables;

import gui.graphic.Drawable;
import gui.graphic.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import representation.Node;
import representation.Operator;
import representation.Port;

public class OpDraw implements Drawable {
	private int x;
	private int y;
	private int sizeX;
	private int sizeY;
	private int portLength = 13;
	private int portHeight = 13;
	private Operator type;
	private String name;
	private Ellipse2D representation;
	private String text;
	private List<InternalPortDraw> ports;
	private int inHeight;
	private int outHeight;
	private int inStartH;
	private int outStartH;
	private int inPortX;
	private int outPortX;

	public OpDraw() {
	}

	public OpDraw(int x, int y, int sizeX, int sizeY, Node node) {
		super();
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.type = node.getOperatorType();
		this.name = node.getId();
		initialize();
	}

	private void initialize() {
		this.representation = new Ellipse2D.Double(x, y, sizeX, sizeY);
		Collection<Port> inPorts = type.getInputs().values();
		Collection<Port> outPorts = type.getOutputs().values();
		this.text = type.getName() + ":" + name;

		inHeight = inPorts.size() * (portHeight + 5);
		outHeight = outPorts.size() * (portHeight + 5);
		int midH = y + sizeY / 2;
		inStartH = midH - inHeight / 2;
		outStartH = midH - outHeight / 2;
		inPortX = (int) (x) - 4;
		outPortX = (int) (x + sizeX) + 4;

		// create port representations
		int i = 0;
		this.ports = new ArrayList<InternalPortDraw>();
		for (Port p : inPorts) {
			ports.add(new InternalPortDraw(p.getName(), x - 4, inStartH + 2 + i
					* (portHeight + 5), portLength, portHeight, true));
			i++;
		}
		i = 0;
		for (Port p : outPorts) {
			ports.add(new InternalPortDraw(p.getName(), x + sizeX + 4,
					outStartH + 2 + +i * (portHeight + 5), portLength,
					portHeight, false));
			i++;
		}
	}

	public void draw(Graphics2D engine) {
		engine.setPaint(Color.green);
		engine.fill(representation);
		engine.setPaint(Color.blue);
		engine.draw(representation);

		int length = engine.getFontMetrics().stringWidth(this.text);
		int height = engine.getFontMetrics().getHeight();

		int posX = x + sizeX / 2 - length / 2;
		int posY = y + sizeY / 2 + height / 3; // + height / 2*/;
		engine.setPaint(Color.BLACK);
		engine.drawString(text, posX, posY);

		// deal with the ports
		// draw input port bar
		engine.drawLine(inPortX, inStartH, inPortX, inStartH + inHeight);
		engine.drawLine(inPortX, inStartH + inHeight / 2, inPortX + 4, inStartH
				+ inHeight / 2);
		// draw output port bar
		engine.drawLine(outPortX, outStartH, outPortX, outStartH + outHeight);
		engine.drawLine(outPortX, outStartH + outHeight / 2, outPortX - 4,
				outStartH + outHeight / 2);
		// draw output ports
		for (InternalPortDraw pd : ports) {
			pd.draw(engine);
			// engine.drawString(outPorts.get(i).getName(), (int) (r.getX() +
			// 2),
			// (int) (r.getY() + height / 2));
			// i++;
		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public Operator getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public String getName() {
		return name;
	}

	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return new Point(this.x + sizeX / 2, this.y + sizeY / 2);
	}

	public List<InternalPortDraw> getPorts() {
		return ports;
	}

	@Override
	public Point getLinkPoint() {
		return this.getCenter();
	}

	public String getTypeName() {
		return this.type.getName();
	}

	@Override
	public boolean contains(Point2D p) {
		return this.representation.contains(p);
	}

	@Override
	public void setPosition(Point2D p) {
		this.x = (int) p.getX() - this.sizeX / 2;
		this.y = (int) p.getY() - this.sizeY / 2;
		initialize();
	}

	@Override
	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
		initialize();
	}

	@Override
	public Point getOrigin() {
		// TODO Auto-generated method stub
		return new Point(x, y);
	}
}
