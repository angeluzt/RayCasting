package forms;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

import geometry.WallWorld;
import utils.Constants;
import utils.Functions;

public class MyCanvas extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private WallWorld world = new WallWorld();
	
	// Change between full, and partial circle, or click on the screen to see different light modes
	private boolean isFullCircle = true;
	
	// Change variables to see different view modes
	private boolean drawLines = false;
	private boolean drawContent = false;
	private boolean drawCircinference = false;
	private boolean drawShadows = true;
	
	// This is to show the image example
	private boolean showImage = true;
	
	
	Rectangle [] rec = new Rectangle[13];
	public MyCanvas() {

        for (int i = 0; i < 13; i++) {
        	rec[i] = new Rectangle(Functions.getRandomNumber(0, Constants.WINDOWS_X), Functions.getRandomNumber(0, Constants.WINDOWS_Y), 
        			20, 20);
		}
	}
	
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        // Background
        g2d.setColor(Constants.BACK_GROUND);
        g2d.fillRect(0, 0, Constants.WINDOWS_X, Constants.WINDOWS_Y);
        
        g2d.setColor(Constants.CIRCLE_COLOR);
        
        for (int i = 0; i < 13; i++) {
        	g2d.fill(rec[i]);
		}
        
        // Draw walls
        g2d.setColor(Constants.LINE_COLOR);
        for(int i = 0; i < world.getWallsLength(); i++) {
            Line2D line = world.getWall(i).getPoints();
            
            g2d.drawLine((int)line.getP1().getX(), (int)line.getP1().getY(), (int)line.getP2().getX(), (int)line.getP2().getY());
        }
        
        g.setColor(Constants.LIGHT_COLOR);
        // Search Intersections, depending of the flag
        if(isFullCircle) {
        	this.world.searchIntersectionsCircularForm(g2d, drawLines, drawShadows, showImage);
        }else{
        	this.world.searchIntersectionsNDegrees(g2d, drawLines, drawContent, drawShadows, showImage);
        }
        
        // Draw circumference or fill figure. It depends of the flags
        if(drawContent && !drawLines) {
        	g.setColor(Constants.LIGHT_COLOR);
        	g.fillPolygon(this.world.getFillX(), this.world.getFillY(), this.world.getFillX().length);
        }else if(drawCircinference) {
        	g.setColor(Constants.LIGHT_COLOR);
        	g.drawPolygon(this.world.getFillX(), this.world.getFillY(), this.world.getFillX().length);
        }
        
        // Draw dot
        g2d.setColor(Constants.CIRCLE_COLOR);
        Point2D lightDot = world.getLightBulb().getLocation();
        g2d.fillOval((int)lightDot.getX() - Constants.LIGHT_BULB_SIZE/2, 
        		(int)lightDot.getY()-Constants.LIGHT_BULB_SIZE/2, 
        		Constants.LIGHT_BULB_SIZE, Constants.LIGHT_BULB_SIZE);
    }
    
    public void repaintCanvas() {
    	this.repaint();
    }
    
    public boolean getIsFullCircle() {
    	return this.isFullCircle;
    }
    
    public WallWorld getWorld() {
    	return this.world;
    }
    
    public void changeIsFullCircle() {
    	this.isFullCircle = !this.isFullCircle;
    }
}
