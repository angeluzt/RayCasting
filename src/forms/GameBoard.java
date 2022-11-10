package forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import utils.Constants;

/**
 *
 * @author angel
 */
public class GameBoard extends javax.swing.JFrame implements MouseMotionListener, KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	MyCanvas canvas = new MyCanvas();

    public GameBoard() {
        initComponents();
        this.createBufferStrategy(3);
        Dimension dimension = new Dimension(Constants.MIN_WINDOWS_X, Constants.MIN_WINDOWS_Y);

        this.setVisible(true);
        this.setResizable(false);
        this.setSize(Constants.WINDOWS_X, Constants.WINDOWS_Y);
        this.setMinimumSize(dimension);
        
        // Define canvas components
        canvas.setMinimumSize(dimension);
        canvas.setSize(Constants.WINDOWS_X - this.getInsets().right - this.getInsets().left, Constants.WINDOWS_Y - this.getInsets().bottom - this.getInsets().top);
        canvas.setBackground(Color.yellow);
        this.add(canvas);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();

        // Add listeners
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		// Only mode full circle has to be controlled on mouse move
		if(this.canvas.getIsFullCircle()) {
			this.getCoordinades(this.getInsets(), e);
			
			this.canvas.repaintCanvas();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Mode full circle do not requires keyboard
		if(!this.canvas.getIsFullCircle()) {
			Point2D point = this.canvas.getWorld().getLightBulb().getLocation();

			if(e.getKeyChar() == 'a') {// Left
				this.canvas.getWorld().getLightBulb().setPoint(new Point2D.Double(point.getX() - Constants.LIGHT_BULB_SPEED, point.getY()));
				this.canvas.getWorld().getLightBulb().setLocation(new Point2D.Double(point.getX() - Constants.LIGHT_BULB_SPEED, point.getY()));
			}else if(e.getKeyChar() == 'd') {// Right
				this.canvas.getWorld().getLightBulb().setPoint(new Point2D.Double(point.getX() + Constants.LIGHT_BULB_SPEED, point.getY()));
				this.canvas.getWorld().getLightBulb().setLocation(new Point2D.Double(point.getX() + Constants.LIGHT_BULB_SPEED, point.getY()));
			}else if(e.getKeyChar() == 'w') {//Up
				this.canvas.getWorld().getLightBulb().setPoint(new Point2D.Double(point.getX(), point.getY() - Constants.LIGHT_BULB_SPEED));
				this.canvas.getWorld().getLightBulb().setLocation(new Point2D.Double(point.getX(), point.getY() - Constants.LIGHT_BULB_SPEED));
			}else if(e.getKeyChar() == 's') {// Down
				this.canvas.getWorld().getLightBulb().setPoint(new Point2D.Double(point.getX(), point.getY() + Constants.LIGHT_BULB_SPEED));
				this.canvas.getWorld().getLightBulb().setLocation(new Point2D.Double(point.getX(), point.getY() + Constants.LIGHT_BULB_SPEED));
			}else if(e.getKeyChar() == 'e') {// rotate clockwise
				this.canvas.getWorld().getLightBulb().setAngle(this.canvas.getWorld().getLightBulb().getAngle() - Constants.LIGHT_BULB_SPEED);
			}else if(e.getKeyChar() == 'q') {// rotate anti-clockwise
				this.canvas.getWorld().getLightBulb().setAngle(this.canvas.getWorld().getLightBulb().getAngle() + Constants.LIGHT_BULB_SPEED);
			}
			this.canvas.repaintCanvas();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void getCoordinades(Insets insets, MouseEvent e) {
		this.canvas.getWorld().getLightBulb().setPoint(new Point2D.Double(e.getX() - insets.left, e.getY()-insets.top));
		this.canvas.getWorld().getLightBulb().setLocation(new Point2D.Double(e.getX() - insets.left, e.getY()-insets.top));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.canvas.changeIsFullCircle();
		this.canvas.repaintCanvas();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
