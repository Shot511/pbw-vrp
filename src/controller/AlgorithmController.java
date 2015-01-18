package controller;

import java.util.ArrayList;

import com.sun.javafx.geom.Point2D;

import model.Depot;
import model.Node;
import model.Solution;

public class AlgorithmController {

	public static int NODES_NO;
	public static int TRUCK_CAPACITY = 54;
	
	public ArrayList<Node> allNodes = new ArrayList<Node>();
	public static int MIN_ROUTES_NO;
	public static Depot depot;
	
	private Solution currentSolution;
	private Solution bestSolution;
	
	public AlgorithmController(ArrayList<Point2D> nodes) {
		//prepare unchangable list of all nodes and changable estimated number of routes/trucks
		setNodes(nodes);
		setMinRoutesNo();
		System.out.println("estimated "+this.MIN_ROUTES_NO+" routes");
		
		currentSolution = new Solution(allNodes);
		bestSolution = new Solution(currentSolution); //deep copy
		for(int i=0;i<10000;i++) {
			System.out.println("iteration "+i);
			currentSolution.modify();
			if(currentSolution.getScore() < bestSolution.getScore())
				bestSolution = new Solution(currentSolution);
//			else
//				currentSolution = new Solution(bestSolution);
		}

	}
	
	//set additional nodes options here
	public void setNodes(ArrayList<Point2D> nodes) {
		int scale = 1;
		NODES_NO = nodes.size();
		depot = new Depot();
		depot.setPositionX((int) (nodes.get(0).x*scale));
		depot.setPositionY((int) (nodes.get(0).y*scale));
		for(int i=1;i<NODES_NO;i++) {
			Node n = new Node(i);
			n.setPositionX((int) (nodes.get(i).x*scale));
			n.setPositionY((int) (nodes.get(i).y*scale));
			allNodes.add(n);
		}
	}
	
	public void setMinRoutesNo() {
		int sum = 0;
		for(int i=0; i<allNodes.size(); i++)
			sum += allNodes.get(i).getBoxNoRequest();
		MIN_ROUTES_NO = (int) Math.ceil(1.0*sum/TRUCK_CAPACITY);
	}
	
	public Solution getBestSolution() {
		return bestSolution;
	}
}
