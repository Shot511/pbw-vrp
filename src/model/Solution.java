package model;

import java.util.ArrayList;

import controller.AlgorithmController;

public class Solution {

	private ArrayList<Route> routesInSolution; //the heart of solution
	private int minRoutesNo = AlgorithmController.MIN_ROUTES_NO;
	private int lastRouteId;
	//private ArrayList<Node> allNodes = (ArrayList<Node>) AlgorithmController.allNodes.clone();
	
	//prepare initial solution
	public Solution() {
		prepareInitialSolution();
	}
	
	//prepare copy of given solution
	public Solution(Solution copyThis) {
		this(copyThis, false);
	}
	
	//either a copy or the modification of the previous solution - this is where magic should be implemented
	public Solution(Solution copyThis, boolean shouldModify) {
		if(!shouldModify) {
			this.setRoutesInSolution((ArrayList<Route>) copyThis.getRoutesInSolution().clone());
		} else {
			
		}
	}
	
	public void prepareInitialSolution() {
		ArrayList<Node> nodesBank = (ArrayList<Node>) AlgorithmController.allNodes.clone();
		
		//prepare set of raw Routes
		this.routesInSolution = new ArrayList<Route>();
		for(int i=0; i<minRoutesNo;i++) {
			Route r = new Route(i+1);
			routesInSolution.add(r);
		}
		lastRouteId = minRoutesNo;
		
		//get each node and assign it to the routes randomly
		while(0<nodesBank.size()) {
			Node n = nodesBank.get((int) (Math.random()*nodesBank.size()-1));
			int count = 0;
			while(count++ < minRoutesNo) {
				Route r = routesInSolution.get((int) (Math.random()*routesInSolution.size()-1));
				if(r.addNode(n)) {
					if(n.isCovered())
						nodesBank.remove(n);
					break;
				} else if (count == minRoutesNo) { //if proper route cannot be found for this node, add route
					Route newRoute = new Route(++lastRouteId);
					newRoute.addNode(n);
					routesInSolution.add(newRoute);
					nodesBank.remove(n);
					System.out.println("was not addable! - count " + count);
				}
			}
		}
	}

	
	public double getScore() {
		return 0;
	}
	
	public ArrayList<Route> getRoutesInSolution() {
		return routesInSolution;
	}

	public void setRoutesInSolution(ArrayList<Route> routesInSolution) {
		this.routesInSolution = routesInSolution;
	}
}
