package gui.graphic.drawables;

import gui.graphic.Drawable;
import gui.graphic.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class PortDraw implements Drawable {
	private int x;
	private int y;
	private int sizeX;
	private int sizeY;
	private String name;
	private Shape representation;
	private boolean input;

	public PortDraw(int x, int y, int sizeX, int sizeY, String name,
			boolean input) {
		super();
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.name = name;
		this.representation = new RoundRectangle2D.Double(x, y, sizeX, sizeY,
				8, 8);
		this.input = input;
	}

	public void draw(Graphics2D engine) {
		engine.setPaint(Color.cyan);
		engine.fill(representation);
		engine.setPaint(Color.red);
		engine.draw(representation);
		engine.setPaint(Color.BLACK);
		int length = engine.getFontMetrics().stringWidth(name);
		int height = engine.getFontMetrics().getHeight();
		int posX = x + sizeX / 2 - length / 2;
		int posY = y + sizeY / 2 + height / 4;
		engine.drawString(name, posX, posY);
		if (!input) {
			engine.drawString("->", x + sizeX, y + sizeY / 2 + height / 4);
		} else {
			engine.drawString("->",
					x - engine.getFontMetrics().stringWidth("->"), y + sizeY
							/ 2 + height / 4);
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

	public String getName() {
		return name;
	}

	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return new Point(x + sizeX / 2, y + sizeY / 2);
	}

	@Override
	public Point getLinkPoint() {
		if (input)
			return new Point(x + sizeX + 3, y + sizeY / 2);

		else
			return new Point(x - 3, y + sizeY / 2);
	}

	@Override
	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return this.representation.contains(p);
	}

	@Override
	public void setPosition(Point2D p) {
		this.x = (int) p.getX() - this.sizeX / 2;
		this.y = (int) p.getY() - this.sizeY / 2;
		this.representation = new RoundRectangle2D.Double(x, y, sizeX, sizeY,
				8, 8);
	}

	@Override
	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
		this.representation = new RoundRectangle2D.Double(x, y, sizeX, sizeY,
				8, 8);
	}

	@Override
	public Point getOrigin() {
		// TODO Auto-generated method stub
		return new Point(x, y);
	}

	public boolean isInput() {
		return this.input;
	}
}
