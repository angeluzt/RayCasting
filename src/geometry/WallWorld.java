package geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import utils.Constants;
import utils.Functions;

public class WallWorld {
	private ArrayList<Wall> gameWalls = new ArrayList<>();
	private LightBulb lightBulb;
	
	private int[] fillX;
	private int[] fillY;
	private int count = 0;
	//Character bfImage = new Character();
	
	public WallWorld() {
		
		this.setUpLightLocation();
		this.setUpWalls();
	}
	
	private void setUpWalls() {

		// Window walls, borders
		addWall(0, 0, 0, Constants.WINDOWS_Y);
		addWall(0, 0, Constants.WINDOWS_X, 0);
		addWall(0, Constants.WINDOWS_Y-48, Constants.WINDOWS_X-19, Constants.WINDOWS_Y-48);
		addWall(Constants.WINDOWS_X-19, 0, Constants.WINDOWS_X-19, Constants.WINDOWS_Y-19);
		
		this.generateRandomLines();
	}
	
	private void generateRandomLines() {
		for (int i = 0; i < Constants.RANDOM_LINES; i++) {
			addWall(Functions.getRandomNumber(0, Constants.WINDOWS_X), Functions.getRandomNumber(0, Constants.WINDOWS_Y), 
					Functions.getRandomNumber(0, Constants.WINDOWS_X), Functions.getRandomNumber(0, Constants.WINDOWS_Y));
		}
	}
	
	/*
	 * Calculate intersection and increase the degrees n (1) times to rotate the point. Repeat process.
	 * 
	 * the result looks like <  (depending of the angle the direction will be different)
	 */
	public void searchIntersectionsNDegrees(Graphics2D g, boolean drawLines, boolean drawContent, boolean drawShadows, boolean showImage) {
		double angle = lightBulb.getAngle();
		Point2D currentLine = lightBulb.getLocation();
		fillX = new int[Constants.PARTIAL_CIRCLE_ANGLE + 1];
		fillY = new int[Constants.PARTIAL_CIRCLE_ANGLE + 1];

		count = 0;
		fillX[count] = (int)lightBulb.getLocation().getX();
		fillY[count] = (int)lightBulb.getLocation().getY();
		count++;

		Point2D distance = new Point2D.Double(currentLine.getX() + Constants.SEARCH_DISTANCE_PARTIAL_CIRCLE, currentLine.getY() + Constants.SEARCH_DISTANCE_PARTIAL_CIRCLE);
		for (int i = lightBulb.getAngle(); i < lightBulb.getAngle() + Constants.PARTIAL_CIRCLE_ANGLE; i++) {
			double[] pt = {distance.getX(), distance.getY()};
			AffineTransform.getRotateInstance(Math.toRadians(angle), currentLine.getX(), currentLine.getY()).transform(pt, 0, pt, 0, 1);
			int newX = (int)pt[0];
			int newY = (int)pt[1];
			
			this.searchPointIntersection(g, new Line2D.Double(currentLine.getX(), currentLine.getY(), newX, newY), lightBulb.getAngle() + Constants.PARTIAL_CIRCLE_ANGLE, drawLines);
			angle += 1;
		}
		
		this.drawShadows(g, drawShadows, showImage);
	}
	
	/*
	 * This will create a circle. The size depends of SEARCH_DISTANCE_CIRCULAR
	 * 
	 * In this case is required to calculate 360 points in order to complete the circumference, an increasing in 1 the degrees
	 */
	public void searchIntersectionsCircularForm(Graphics2D g, boolean drawLines, boolean drawShadows, boolean showImage) {
		double angle = 0;
		double steps = (double)360 / Constants.COMPARATION_STEPS;
		
		Point2D currentLine = lightBulb.getLocation();
		
		Point2D distance = new Point2D.Double(currentLine.getX() + Constants.SEARCH_DISTANCE_CIRCULAR, currentLine.getY() + Constants.SEARCH_DISTANCE_CIRCULAR);
		fillX = new int[Constants.COMPARATION_STEPS];
		fillY = new int[Constants.COMPARATION_STEPS];
		count = 0;
		for (int i = 0; i < Constants.COMPARATION_STEPS; i++) {
			double[] pt = {distance.getX(), distance.getY()};
			AffineTransform.getRotateInstance(Math.toRadians(angle), currentLine.getX(), currentLine.getY()).transform(pt, 0, pt, 0, 1);
			int newX = (int)pt[0];
			int newY = (int)pt[1];
			
			boolean result = this.searchPointIntersection(g, new Line2D.Double(currentLine.getX(), currentLine.getY(), newX, newY), 360, drawLines);
			
			// Save and draw the points with no intersection
			if(!result) {
				
				if(drawLines)
					g.drawLine((int)currentLine.getX(), (int)currentLine.getY(), newX, newY);
				if(count < fillX.length) {
					fillX[count] = newX;
					fillY[count] = newY;
					count++;
				}
			}
			angle += steps;
		}
		
		this.drawShadows(g, drawShadows, showImage);
	}
	
	/*
	 * Calculate intersection between current line to evaluate.
	 * 
	 * The center is the lightDot, and the other point is the one calculated in previous methods
	 */
	private boolean searchPointIntersection(Graphics2D g, Line2D lineToEvaluate, int size, boolean drawLines) {
		Point2D currentPoint = null;
		int currentDistance = 0;
		for (int i = 0; i < gameWalls.size(); i++) {
			Line2D line = this.getWall(i).getPoints();
			
			Point2D result = this.calculateIntersectionPoint(lineToEvaluate.getP1(), lineToEvaluate.getP2(), line.getP1(), line.getP2());

			// If result is null, no intersection found
			if(result != null) {
				
				// Take the first point when no point selected
				if(currentPoint == null) {
					currentPoint = result;
					currentDistance = Functions.distanceBetweenPoints(this.lightBulb.getLocation(), result);
					
				}else {
					// Else, calculate the distance between lightDot and the intersected point.
					// if the new distance is lower, that means the new intersection is closer the lightDot, only the 
					// closer one has to be selected to avoid showing the light in two lines at the same time
					int tempDistance = Functions.distanceBetweenPoints(this.lightBulb.getLocation(), result);
					if(tempDistance < currentDistance) {
						currentPoint = result;
						currentDistance = tempDistance;
					}
				}
			}
				
		}
		
		// To avoid null pointer
		if(currentPoint != null) {
			// Only draw lines if the flag is active
			if(drawLines)
					g.drawLine((int)lineToEvaluate.getP1().getX(), (int)lineToEvaluate.getP1().getY(), (int)currentPoint.getX(), (int)currentPoint.getY());
			
			// Save all the points with intersection.
			// Those points will be used to draw the circumference if the flag is activated in MyCanvas class
			if(count < fillX.length) {
				fillX[count] = (int)currentPoint.getX();
				fillY[count] = (int)currentPoint.getY();
				count++;
			}
			return true;
		}else {
			return false;
		}
	}
	
	private void drawShadows(Graphics2D g2d, boolean draw, boolean showImage) {
		// Draw shadows only when the flag is active
		if(!draw)
			return;
		
		g2d.setColor(new Color(0,0,0,Constants.SHADOW_ALPHA));
        Area a1 = new Area(new Polygon(fillX, fillY, count));
        Area a2 = new Area(new Rectangle(0, 0, Constants.WINDOWS_X, Constants.WINDOWS_Y));
        a2.subtract(a1);
        g2d.fill(a2);

        /*if(showImage)
        	this.paintImage(g2d, a1);*/
	}
	
	
	/*public void paintImage(Graphics2D g2d, Area area) {
		g2d.setColor(Constants.LIGHT_COLOR);
		for (int y = 0; y < bfImage.bfImage.getHeight(); y++) {
		    for (int x = 0; x < bfImage.bfImage.getWidth(); x++) {
		    	if(area.contains(x + bfImage.x, y + bfImage.y)) {
		    	    Color color = new Color(bfImage.bfImage.getRGB(x, y), true);

		    		if(color.getAlpha() > 0) { 
		    			g2d.setColor(color);
		    			g2d.drawLine(x + bfImage.x, y + bfImage.y, x + bfImage.x, y + bfImage.y);
		    		}
		    	}
		    }
		}
	}*/
	
	private Point2D calculateIntersectionPoint(Point2D A, Point2D B, Point2D C, Point2D D) {
		
		/* If lines are not intersected, end process.
		   if this if is removed, the code bellow will calculate an intersection considering that the lines tend to infinite
		   and will not find an intersection when lines are parallel
		  
		  Then this if is used to calculate the the lines (considering its real size) are intersected 
		*/
		if(!Functions.areLinesIntersected(A, B, C, D)) {
			return null;
		}
		
		// Line AB represented as a2x + b2y = c1
		double a1 = B.getY() - A.getY(); 
        double b1 = A.getX() - B.getX(); 
        double c1 = a1*(A.getX()) + b1*(A.getY()); 
       
        // Line CD represented as c2x + d2y = c2 
        double a2 = D.getY() - C.getY(); 
        double b2 = C.getX() - D.getX(); 
        double c2 = a2*(C.getX())+ b2*(C.getY()); 
       
        double determinant = a1*b2 - a2*b1; 
       
        if (determinant == 0) 
        { 
            // The lines are parallel. This is simplified
        	return null;
        } 
        else
        { 
        	// Calculate the intersection point
            double x = (b2*c1 - b1*c2)/determinant; 
            double y = (a1*c2 - a2*c1)/determinant; 
            return new Point2D.Double(x, y); 
        } 
	}
	
	private void setUpLightLocation() {
		lightBulb = new LightBulb(20, 20);
	}
	
	public void addWall(int x1, int y1, int x2, int y2) {
		this.gameWalls.add(new Wall(x1, y1, x2, y2));
	}
	
	public Wall getWall(int index) {
		return gameWalls.get(index);
	}
	
	public int getWallsLength() {
		return this.gameWalls.size();
	}

	public LightBulb getLightBulb() {
		return this.lightBulb;
	}
	
	public int[] getFillX() {
		return this.fillX;
	}
	
	public int[] getFillY() {
		return this.fillY;
	}
}
