package helpers;

import java.util.ArrayList;

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
	
	public boolean isAlreadyTheSameLine(ArrayList<NodeConnection> list, int j)
	{
		for(int i = 0; i < list.size(); ++i)
		{
			if (i == j || list.get(i).getCarID() == carID)
				return false;
			
			if(list.get(i).getStart().x == start.x && list.get(i).getStart().y == start.y && list.get(i).getEnd().x == end.x && list.get(i).getEnd().y == end.y)
				return true;
		}
		return false;
	}
	
}
