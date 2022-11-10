package geometry;

import java.awt.geom.Line2D;

public class Wall {
	private Line2D line;
	
	public Wall() {
	}
	
	public Wall(int x1, int y1, int x2, int y2) {
		this.line = new Line2D.Double(x1, y1, x2, y2);
	}
	
	public Line2D getPoints() {
		return line;
	}
}
