package view;

import helpers.NodeConnection;
import helpers.PoissonDisk;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D.Double;
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
	
	public void generateSomeLines()
	{
		if(nodes != null)
		{
			if(lines != null)
				lines.clear();
			else
				lines = new ArrayList<NodeConnection>();
				
			int carID = 1;
			for(int i = 1; i < nodes.size(); ++i)
			{
				NodeConnection nc = new NodeConnection(nodes.get(i-1), nodes.get(i), carID);
				lines.add(nc);
				carID += 50;
			}
			
			repaint();
		}
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
        
        if(lines != null)
        {        
        	HsvColor hsvColor = new HsvColor();
        	hsvColor.s = 0.99;
        	hsvColor.v = 0.99;
        	
	        for(int i = 0; i < lines.size(); ++i)
	        {
	        	double PHI = (1.0 + Math.sqrt(5.0))/2.0;
	        	double n = lines.get(i).getCarID() * PHI - Math.floor(lines.get(i).getCarID() * PHI);
	        	hsvColor.h = Math.floor(n * 360);
	        	
	        	g2.setColor(hsvColor.hsv2rgb());
	        	g2.draw(new Line2D.Float(lines.get(i).getStart().x, 
	        							 lines.get(i).getStart().y,
	        							 lines.get(i).getEnd().x,
	        							 lines.get(i).getEnd().y));
	        }
        }
	}
	
	public ArrayList<Point2D> getNodes()
	{
		return nodes;
	}
	
	public void setLines(ArrayList<NodeConnection> lines)
	{
		this.lines = lines;
	}
	
	public ArrayList<NodeConnection> getLines()
	{
		return lines;
	}

	public int getNodeSize() {
		return nodeSize;
	}
	
	class HsvColor
	{
		public double h;
		public double s;
		public double v;
		
		public Color hsv2rgb()
		{
			Color c = null;
			
			if(v == 0)
				c = new Color(0, 0, 0);
			else
			{
				h /= 60.0;
				double i = Math.floor(h);
				double f = h - i;
				double p = v * (1.0 - s);
				double q = v * (1.0 - (s*f));
				double t = v * (1.0 - (s * (1.0 - f)));
				
				if(i == 0) { c = new Color((float)v, (float)t, (float)p); }
				else if (i == 1) { c = new Color((float)q, (float)v, (float)p); }
				else if (i == 2) { c = new Color((float)p, (float)v, (float)t); }
				else if (i == 3) { c = new Color((float)p, (float)q, (float)v); }
				else if (i == 4) { c = new Color((float)t, (float)p, (float)v); }
				else if (i == 5) { c = new Color((float)v, (float)p, (float)q); }
			}
			
			return c;
		}
	}
}
