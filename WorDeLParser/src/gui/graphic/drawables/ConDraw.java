package gui.graphic.drawables;

import gui.graphic.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class ConDraw {
	private String name;
	private Point origin;
	private List<Point> destinations;

	public ConDraw(String name, Point origin, List<Point> destinations) {
		super();
		this.name = name;
		this.origin = origin;
		this.destinations = destinations;
	}

	public ConDraw(String name, Point origin) {
		super();
		this.name = name;
		this.origin = origin;
		this.destinations = new ArrayList<Point>();
	}

	public void addDestination(Point destination) {
		this.destinations.add(destination);
	}

	public void draw(Graphics2D engine) {
		if (destinations != null && !destinations.isEmpty()) {

			for (Point d : destinations) {
				if (d != null) {
					engine.setPaint(Color.black);
					engine.drawLine(origin.getX(), origin.getY(), d.getX(),
							d.getY());
					engine.drawString(this.name,
							(origin.getX() + d.getX()) / 2,
							(origin.getY() + d.getY()) / 2);
					engine.setPaint(Color.red);
					engine.fillOval(d.getX() - 3, d.getY() - 3, 6, 6);
				}
			}
		}
	}
}
