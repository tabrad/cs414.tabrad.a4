package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GraphicPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	//paintComponent is called in repaint()
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		BufferedImage img = null;
		try 
		{
		    img = ImageIO.read(GarageView.class.getResource("images/sports-car.png"));
		} catch (IOException e) {
		}

		g.drawImage(img, 0, 0, null);
	}
}
