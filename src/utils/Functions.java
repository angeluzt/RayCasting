package utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public class Functions {

	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public static boolean areLinesIntersected(Point2D A, Point2D B, Point2D C, Point2D D) {
		if(!Line2D.linesIntersect(A.getX(),A.getY(),B.getX(),B.getY(),C.getX(),C.getY(),D.getX(),D.getY())) {
			return false;
		}else {
			return true;
		}
	}
	
	public static int distanceBetweenPoints(Point2D p1, Point2D p2) {
		return (int)Point2D.distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
}
