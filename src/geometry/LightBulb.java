package geometry;

import java.awt.geom.Point2D;

public class LightBulb {
	private Point2D location;
	private int angle = 0;
	
	public LightBulb() {
	}
	
	public LightBulb(int x, int y) {
		location = new Point2D.Double(x, y);
	}
	
	public void setPoint(Point2D location) {
		this.location = location;
	}
	
	public Point2D getLocation() {
		return this.location;
	}
	
	public void setLocation(Point2D location) {
		this.location = location;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public int getAngle() {
		return this.angle;
	}
}
