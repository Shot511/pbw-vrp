package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapPanel extends JPanel 
{
	private BufferedImage image = null;
	
	public MapPanel()
	{		
		try {
			image = ImageIO.read(new File("img/Poland.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);
        if(image != null)
        {
            g.drawImage(image, 0, 0, this);
        }
	}
}
