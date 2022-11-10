package geometry;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Character {
	int width, height;
	BufferedImage bfImage;
    int x = 300, y = 300;
	Rectangle rectangleImage;
	
	public Character() {
		rectangleImage = new Rectangle(x, y, 160, 210);
		
		try {
			bfImage = ImageIO.read(getClass().getResource("/pikachu.png"));
		} catch (IOException e) {
		}
	}
	
}
