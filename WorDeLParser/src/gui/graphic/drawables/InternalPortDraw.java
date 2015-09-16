package gui.graphic.drawables;

import gui.graphic.Drawable;
import gui.graphic.Point;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class InternalPortDraw implements Drawable {
	private String name;
	private int x;
	private int y;
	private int sizeX;
	private int sizeY;
	private boolean input;
	private Rectangle2D representation;
	private int w;

	public InternalPortDraw(String name, int x, int y, int sizeX, int sizeY,
			boolean input) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.input = input;
		w = name.length() * 7;
	}

	public void draw(Graphics2D engine) {
		int h = engine.getFontMetrics().getHeight();
		if (this.representation == null) {
			if (input) {
				this.representation = new Rectangle2D.Double(x - w - 4, y,
						w + 4, sizeY);
			} else {
				this.representation = new Rectangle2D.Double(x, y, w + 4, sizeY);
			}
		}
		engine.draw(representation);
		if (input)
			engine.drawString(name, x - w - 2, y + h / 2 + 2);
		else
			engine.drawString(name, x + 2, y + h / 2 + 2);
		// engine.drawLine(x, y + h-3, x + 10, y + h-3);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
			return new Point(x - w - 4, y + sizeY / 2);
		else
			return new Point(x + w + 4, y + sizeY / 2);
	}

	@Override
	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPosition(Point2D p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	@Override
	public Point getOrigin() {
		// TODO Auto-generated method stub
		return new Point(x, y);
	}
}
