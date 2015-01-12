package helpers;

import com.sun.javafx.geom.Point2D;

public class NodeConnection 
{
	Point2D start;
	Point2D end;
	int carID;
	
	public NodeConnection(Point2D start, Point2D end, int carID)
	{
		this.start = start;
		this.end = end;
		this.carID = carID;
	}

	public Point2D getStart() 
	{
		return start;
	}

	public Point2D getEnd() 
	{
		return end;
	}

	public int getCarID() 
	{
		return carID;
	}
	
}
