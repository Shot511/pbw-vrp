package view;

import helpers.NodeConnection;
import helpers.PoissonDisk;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.sun.javafx.geom.Point2D;

public class MapPanel extends JPanel 
{
	private BufferedImage image = null;
	private int width, height;
	private int nodeSize;
	
	private ArrayList<Point2D> nodes;
	private ArrayList<NodeConnection> lines;
	
	public MapPanel()
	{		
		nodeSize = 10;
		
		try {
			image = ImageIO.read(new File("img/Poland.png"));
			width = image.getWidth();
			height = image.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void generateNodes(int kPoints, double minDistance)
	{
		if(nodes != null)
			nodes.clear();
		
		if(lines != null)
			lines.clear();
		
		Point2D firstPoint = new Point2D(width/2, height/2);
		PoissonDisk ps = new PoissonDisk(firstPoint, kPoints, width-20, height-20);
		
		nodes = ps.generatePoints(minDistance);
		repaint();
	}

	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
        if(image != null)
        {
            g2.drawImage(image, 0, 0, this);
        }
        
        if(nodes != null)
        {
	        g2.setColor(Color.RED);
	        g2.fill(new Ellipse2D.Double((int)nodes.get(0).x-nodeSize/2, (int)nodes.get(0).y-nodeSize/2, nodeSize, nodeSize));
	        
	        g2.setColor(Color.BLUE);
	        for(int i = 1; i < nodes.size(); ++i)
	        	g2.fill(new Ellipse2D.Double((int)nodes.get(i).x-nodeSize/2, (int)nodes.get(i).y-nodeSize/2, nodeSize, nodeSize));
	        	
        }
	}
	
	public ArrayList<Point2D> getNodes()
	{
		return nodes;
	}
	
	public ArrayList<NodeConnection> getLines()
	{
		return lines;
	}

	public int getNodeSize() {
		return nodeSize;
	}
}
