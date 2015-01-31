package controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import helpers.Log;

import javax.swing.JLabel;

import view.MainFrame;

import com.sun.javafx.geom.Point2D;

import model.Depot;
import model.Node;
import model.Route;
import model.Solution;

public class AlgorithmController {

	private static Logger LOGGER_A = Logger.getLogger("algorithm");
	private static Logger LOGGER_R = Logger.getLogger("routes");
	public static int NODES_NO;
	public static int TRUCK_CAPACITY;// = 54;
	
	public ArrayList<Node> allNodes = new ArrayList<Node>();
	public static int MIN_ROUTES_NO;
	public static Depot depot;
	
	private Solution currentSolution;
	private Solution bestSolution;
	
	public AlgorithmController(ArrayList<Point2D> nodes, JLabel label) 
	{
		TRUCK_CAPACITY = MainFrame.INSTANCE.getTruckCapacity();
		
		//prepare unchangable list of all nodes and changable estimated number of routes/trucks
		setNodes(nodes);
		setMinRoutesNo();
		System.out.println("estimated "+AlgorithmController.MIN_ROUTES_NO+" routes");
		
		currentSolution = new Solution(allNodes);
		bestSolution = new Solution(currentSolution); //deep copy
		
		for(int repeat=0; repeat<10; repeat++) {
			setNodes(nodes);
			setMinRoutesNo();
			currentSolution = new Solution(allNodes);
			for(int i=0;i<100;i++) {
				currentSolution.modify();
				LOGGER_A.info(Integer.toString(currentSolution.getScore()));
				if(currentSolution.getScore() < bestSolution.getScore())
					bestSolution = new Solution(currentSolution);
//				else
//					currentSolution = new Solution(bestSolution);
			}
		}
		logRouteInfo();
	}
	
	private void logRouteInfo() {
		ArrayList<Route> routes = bestSolution.getRoutesInSolution();
		for(int i=0; i<routes.size(); i++) {
			Route r = routes.get(i);
			ArrayList<Node> nodes = r.getNodesOnRoute();
			LOGGER_R.info("Route "+(i+1)+"\r\nNumber of nodes: "+ (r.getNodesNumber()-2));
			LOGGER_R.info("Total truck load: "+r.getFinalTruckLoad());
			LOGGER_R.info("On route from - to: "+r.getStartHours().get(0)+":"+r.getStartMins().get(0)+" - "
			+r.getEndHours().get(nodes.size()-1)+":"+r.getEndMins().get(nodes.size()-1));
			for(int j=1; j<nodes.size()-1; j++) {
				Node n = nodes.get(j);
				LOGGER_R.info("Node "+j+" (X: "+n.getPositionX()+", Y: "+n.getPositionY()+")");
				LOGGER_R.info("\tBoxes left: "+r.getBoxesDelivered().get(j));
				LOGGER_R.info("\tUnpacking from - to: "+r.getStartHours().get(j)+":"+r.getStartMins().get(j)+" - "
				+r.getEndHours().get(j)+":"+r.getEndMins().get(j));
			}
			LOGGER_R.info("");
		}
	}

	//set additional nodes options here
	public void setNodes(ArrayList<Point2D> nodes) {
		allNodes.clear();
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
