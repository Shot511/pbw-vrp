package view;

import helpers.NodeConnection;
import helpers.PoissonDisk;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Node;
import model.Route;
import model.Solution;

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
	
	public void generateSolutionLines(Solution solution)
	{
		if(lines != null)
			lines.clear();
		else
			lines = new ArrayList<NodeConnection>();
		
		ArrayList<Route> routes = solution.getRoutesInSolution();
		for(int i=0; i<routes.size(); i++) {	//for(int i=0; i<5; i++) {
			ArrayList<Node> nodesInRoute = routes.get(i).getNodesOnRoute();
			int carID = routes.get(i).getId();
			for(int j=0; j<nodesInRoute.size()-1; j++) {
				Node n1 = nodesInRoute.get(j);
				Node n2 = nodesInRoute.get(j+1);
				Point2D p1 = new Point2D(n1.getPositionX(),n1.getPositionY());
				Point2D p2 = new Point2D(n2.getPositionX(),n2.getPositionY());
				NodeConnection nc = new NodeConnection(p1, p2, carID);
				lines.add(nc);
			}
		}
			
		repaint();
	}

	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
        if(image != null)
        {
            g2.drawImage(image, 0, 0, this);
        }
        
        if(lines != null)
        {        
        	HsvColor hsvColor = new HsvColor();
        	hsvColor.s = 0.99;
        	hsvColor.v = 0.99;
        	
	        for(int i = 0; i < lines.size(); ++i)
	        {
	        	g2.setStroke(new BasicStroke(2));
	        	double PHI = (1.0 + Math.sqrt(5.0))/2.0;
	        	double n = lines.get(i).getCarID() * PHI - Math.floor(lines.get(i).getCarID() * PHI);
	        	hsvColor.h = Math.floor(n * 360);
	        	
	        	g2.setColor(hsvColor.hsv2rgb());
	        	
	        	if(lines.get(i).isAlreadyTheSameLine(lines, i))
	        		g2.setStroke(new BasicStroke(0.85f));
	        	
	        	g2.draw(new Line2D.Float(lines.get(i).getStart().x, 
	        							 lines.get(i).getStart().y,
	        							 lines.get(i).getEnd().x,
	        							 lines.get(i).getEnd().y));
	        	double theta = Math.atan2(lines.get(i).getEnd().y - lines.get(i).getStart().y, lines.get(i).getEnd().x - lines.get(i).getStart().x);
	        	drawArrow(g2, theta, (lines.get(i).getStart().x + lines.get(i).getEnd().x)/2, (lines.get(i).getStart().y + lines.get(i).getEnd().y)/2);
	        }
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
	
	private void drawArrow(Graphics2D g2, double theta, double x0, double y0)
    {
		int barb = 7;
		double phi = Math.PI / 6;
		
		//g2.setColor(Color.black);
		
        double x = x0 - barb * Math.cos(theta + phi);
        double y = y0 - barb * Math.sin(theta + phi);
        g2.draw(new Line2D.Double(x0, y0, x, y));
        x = x0 - barb * Math.cos(theta - phi);
        y = y0 - barb * Math.sin(theta - phi);
        g2.draw(new Line2D.Double(x0, y0, x, y));
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
