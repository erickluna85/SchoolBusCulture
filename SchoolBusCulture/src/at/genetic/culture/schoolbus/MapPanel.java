package at.genetic.culture.schoolbus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.beans.Transient;

import javax.swing.JPanel;

public class MapPanel extends JPanel {
	private static final long serialVersionUID = 3927006095992144643L;
	private SchoolArea area;
	private int w = 800;
	private int h = 800;
	private BusStop[] route = null;

	public MapPanel(SchoolArea area) {
		this.area = area;
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		return new Dimension(w + 40, h + 40);
	}
	
	public void addPath(Integer[] path) {
		route = area.getRoute(path);
		repaint();
	}
	
	public void paintRoute() {
		BusStop[] stops = area.getAllStops();
		int[] max = getMax(stops);
		Graphics2D g2 = (Graphics2D) this.getGraphics();

		Point p;
		Point p2;
	    g2.setStroke(new BasicStroke(2));
		int c = -1;
		Color[] color = new Color[] {Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.GRAY,  Color.PINK, Color.YELLOW, Color.RED};
		for (int i = 1; i < route.length; i++) {
			if (route[i-1] == area.school) {
				c = (c+1)%color.length;
				g2.setPaint(color[c]);
			}
			p = project(route[i-1], max[0], max[1], max[2], max[3]);
			p2 = project(route[i], max[0], max[1], max[2], max[3]);
			g2.drawLine(p.x, p.y, p2.x, p2.y);
			try {
				  Thread.sleep(100L);    // 100 miliseconds
				}
				catch (Exception e) {}
		}

		p = project(stops[0], max[0], max[1], max[2], max[3]);
		g2.setPaint(Color.RED);
		g2.fillOval(p.x-5, p.y-5, 10, 10);
		
	}

	public void paint(Graphics g) {

		Dimension dim = getSize();
		h = dim.height - 40;
		w = dim.width - 40;

		BusStop[] stops = area.getAllStops();
		int[] max = getMax(stops);
		Graphics2D g2 = (Graphics2D) g;

		Point p;
		g2.setPaint(Color.RED);
		for (int i = 0; i < stops.length; i++) {
			p = project(stops[i], max[0], max[1], max[2], max[3]);
			if (i>0) {
				int size = (int) Math.sqrt(((double)(32*stops[i].numberOfPupils))/Math.PI);
				g2.fillOval(p.x-size/2, p.y-size/2, size, size);
			}
			else {
				g2.fillRect(p.x-7, p.y-7, 14, 14);
				g2.setPaint(Color.BLUE);
			}
		}

	}

	private int[] getMax(BusStop[] stops) {
		Integer maxX = null, minX = null, maxY = null, minY = null;
		for (BusStop busStop : stops) {
			if (maxX == null) maxX = busStop.x;
			if (minX == null) minX = busStop.x;
			if (maxY == null) maxY = busStop.y;
			if (minY == null) minY = busStop.y;
			
			if (busStop.x > maxX)
				maxX = busStop.x;
			else if (busStop.x < minX)
				minX = busStop.x;
			if (busStop.y > maxY)
				maxY = busStop.y;
			else if (busStop.y < minY)
				minY = busStop.y;
		}
		
		int diffX = minX - maxX;
		int diffY = minY - maxY;
		//int diff = (diffX > diffY ? diffX : diffY);

		int xoffset = minX + (maxX - minX) / 2;
		int yoffset = minY + (maxY - minY) / 2;
		
		return new int[] { diffX, diffY, xoffset, yoffset };
	}

	private Point project(BusStop stop, int diffX, int diffY, int xoffset, int yoffset) {
		double scaleX = (double) w / (double) diffX;
		double scaleY = (double) h / (double) diffY;
		double shiftX = ((double) w / 2.0) + 20;
		double shiftY = ((double) h / 2.0) + 20;
		
		double scale = (scaleX > scaleY ? scaleX : scaleY);

		int x = (int) (((double) stop.x - (double) xoffset) * scale + shiftX);
		int y = (int) (((double) stop.y - (double) yoffset) * scale + shiftY);

		Point p = new Point(x, y);
		return p;
	}
}
