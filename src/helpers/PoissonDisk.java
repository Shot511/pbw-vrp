package helpers;

import java.util.ArrayList;
import java.util.Random;

import com.sun.javafx.geom.Point2D;

public class PoissonDisk 
{
	private int kPoints, generatedPoints, mapWidth, mapHeight;
	private ArrayList<Point2D> outputList;
	
	public PoissonDisk(Point2D firstPoint, int kPoints, int mapWidth, int mapHeight)
	{
		this.kPoints = kPoints;
		this.generatedPoints = 0;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		this.outputList = new ArrayList<Point2D>();
		this.outputList.add(firstPoint);
	}
	
	public ArrayList<Point2D> generatePoints(double minDistance)
	{
		generatedPoints = 0;
		Random rand = new Random();
		
		while(kPoints > generatedPoints)
		{
			//Generate random point around
			Point2D newPoint = new Point2D(rand.nextFloat()*mapWidth, rand.nextFloat()*mapHeight);
				
			//check if point is ok to be added or not
			if(!isInNeighbourhood(newPoint, minDistance) && isInMap(newPoint))
			{
				outputList.add(newPoint);
				++generatedPoints;
			}
		}
		
		return outputList;
	}
	
	private boolean isInNeighbourhood(Point2D newPoint, double minDistance)
	{
		for(int i = 0; i < outputList.size(); ++i)
		{
			if(outputList.get(i).distance(newPoint) < minDistance)
				return true;
		}
		
		return false;
	}
	
	private boolean isInMap(Point2D M) 
	{
		int f = 70;
		Point2D A = new Point2D(f, f);
		Point2D B = new Point2D(mapWidth-f, f);
		Point2D D = new Point2D(f, mapHeight-f);
		
		Point2D AM = new Point2D(M.x - A.x, M.y - A.y);
		Point2D AB = new Point2D(B.x - A.x, B.y - A.y);
		Point2D AD = new Point2D(D.x - A.x, D.y - A.y);
		
		float dotAMAB = dot(AM, AB);
		float dotAMAD = dot(AM, AD);
		if(M.y>0.55*M.x+(mapHeight*3/5)) return false;
		
		return (0 < dotAMAB && dotAMAB < dot(AB, AB)) &&
			   (0 < dotAMAD && dotAMAD < dot(AD, AD));
	}
	
	private float dot(Point2D a, Point2D b)
	{
		return a.x*b.x + a.y*b.y;
	}
	
	
}
