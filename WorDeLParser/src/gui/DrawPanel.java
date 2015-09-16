package gui;

import gui.graphic.drawables.FlowDraw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import representation.nodes.FlowNode;

public class DrawPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	public static Map<String, FlowDraw> positions = new HashMap<String, FlowDraw>();
	private int prefferedWidth;
	private int prefferedHeight;
	private Collection<FlowNode> flows;
	private List<FlowDraw> flowDraws;
	private FlowDraw selected;

	private double scale = 1.0;
	// private Drawable selected;
	private java.awt.Point origin;
	private Point pressed;

	public DrawPanel(int prefferedWidth, int prefferedHeight) {
		super();
		this.prefferedWidth = prefferedWidth;
		this.prefferedHeight = prefferedHeight;
		this.flowDraws = new ArrayList<FlowDraw>();
		this.flows = new ArrayList<FlowNode>();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void zoomIn() {
		// this.prefferedWidth += this.prefferedWidth * 0.3;
		// this.prefferedHeight += this.prefferedHeight * 0.3;
		scale += 0.1;
	}

	public void zoomOut() {
		// this.prefferedWidth += this.prefferedWidth * 0.3;
		// this.prefferedHeight += this.prefferedHeight * 0.3;
		if (scale > 0.5)
			scale -= 0.1;
	}

	public void reset() {
		if (this.flows != null)
			this.setFlows(flows);
	}

	public void setFlows(Collection<FlowNode> flows) {
		if (flows != null && !flows.isEmpty()) {
			this.flowDraws.clear();
			this.flows = flows;
			int offsetY = 10;
			int offsetX = 10;
			for (FlowNode fn : this.flows) {

				FlowDraw fd = positions.get(fn.getId());
				if (fd != null)
					fd.setFlow(fn);
				if (fd == null) {
					fd = new FlowDraw(offsetX, offsetY, fn);

					offsetY += fd.getHeight() + 20;
				}
				repaint();
				this.flowDraws.add(fd);
			}
		}
	}

	public void restart() {
		this.positions.clear();
		this.flows.clear();
		this.flowDraws.clear();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gd = (Graphics2D) g;
		gd.scale(scale, scale);

		if (flowDraws != null && !flowDraws.isEmpty()) {
			for (FlowDraw fd : flowDraws)
				fd.draw(gd);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.prefferedWidth, this.prefferedHeight);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		java.awt.Point p = arg0.getPoint();
		int dx = (int) (p.getX() - pressed.getX());
		int dy = (int) (p.getY() - pressed.getY());
		if (this.selected != null)
			this.selected.move(dx, dy);
		this.pressed = p;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		java.awt.Point p = arg0.getPoint();
		boolean sel = false;
		if (this.flowDraws != null && !flowDraws.isEmpty()) {
			for (FlowDraw fd : flowDraws) {
				if (fd.contains(p)) {
					fd.selectFromPoint(p);
					this.selected = fd;
					sel = true;
				}
			}

		}
		if (!sel)
			selected = null;
		this.pressed = p;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (this.selected != null)
			this.positions.put(selected.getName(), selected);
	}

}