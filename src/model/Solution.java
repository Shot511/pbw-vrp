package model;

import java.util.ArrayList;

import controller.AlgorithmController;

public class Solution {

	private ArrayList<Route> routesInSolution = new ArrayList<Route>(); //the heart of solution
	private int minRoutesNo = AlgorithmController.MIN_ROUTES_NO;
	private int lastRouteId;
	private ArrayList<Node> allNodes;
	
	//prepare initial solution
	public Solution(ArrayList<Node> allNodes) {
		this.allNodes = allNodes;
		prepareInitialSolution();
	}
	
	//prepare copy of given solution
	public Solution(Solution copyThis) {
		this.lastRouteId = copyThis.lastRouteId;
		this.allNodes = new ArrayList<Node>();
		
		for(int i=0; i<copyThis.routesInSolution.size(); i++) {
			this.routesInSolution.add(new Route(copyThis.routesInSolution.get(i)));
			for(Route route : routesInSolution)
				for(int j=0; j<route.getNodesOnRoute().size(); j++)
					if(this.allNodes.contains(route.getNodesOnRoute().get(j)))
						this.allNodes.add(copyThis.allNodes.get(j));
		}
	}
	
	public void prepareInitialSolution() {
		ArrayList<Node> nodesBank = (ArrayList<Node>) allNodes.clone();
		
		//prepare set of raw Routes
		for(int i=0; i<minRoutesNo;i++) {
			Route r = new Route(i+1);
			routesInSolution.add(r);
		}
		lastRouteId = minRoutesNo;
		
		//get each node and assign it to the routes randomly
		while(0<nodesBank.size()) {
			Node n = nodesBank.get((int) (Math.random()*nodesBank.size()-1));
			int count = 0;
			while(count++ < minRoutesNo) { //can be improved later by checking all routes
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
					System.out.println("Node was not addable to any route! Creating new route...");
				}
			}
		}
		removeEmptyRoutes();
	}
	
	//in progress
	public void modify() {
		ArrayList<Node> nodesBank = (ArrayList<Node>) this.allNodes.clone();
		ArrayList<Node> nodesToRemove = new ArrayList<Node>();
		int initialSize = nodesBank.size();
		
		//remove 20% of nodes from routes
		while(nodesBank.size()>0.8*initialSize) {
			Node n = nodesBank.get((int) (Math.random()*nodesBank.size()-1));
			ArrayList<Integer> routesAssigned = n.getRouteIds();
			for(int i=0; i<this.routesInSolution.size(); i++)
				if(routesAssigned.contains(this.routesInSolution.get(i).getId()))
					this.routesInSolution.get(i).removeNode(n);
			
			n.resetNode();
			nodesToRemove.add(n);
			nodesBank.remove(n);
		}
		
		//find new routes for the removed nodes
		while(0<nodesToRemove.size()) {
			ArrayList<Route> routesBank = (ArrayList<Route>) routesInSolution.clone();
			Node n = nodesToRemove.get((int) (Math.random()*nodesToRemove.size()-1));
			while(routesBank.size()>0) {
				Route bestRoute = routesBank.get(0);
				int mindelta = routesBank.get(0).getScoreDeltaAfterAdding(n);
				for(int r=0; r<routesBank.size(); r++) {
					int delta = routesBank.get(r).getScoreDeltaAfterAdding(n); 
					if(delta < mindelta) {
						mindelta = delta;
						bestRoute = routesBank.get(r);
					}
				}
				if(bestRoute.addNode(n)) {
					routesBank.remove(bestRoute);
					break;
				}
				else if (routesBank.size()==1) {
					Route r = new Route(++lastRouteId);
					r.addNode(n);
					routesInSolution.add(r);
					break;
				}
				routesBank.remove(bestRoute);
			}
			if(n.isCovered())
				nodesToRemove.remove(n);
		}
		removeEmptyRoutes();
	}
	
	public void removeEmptyRoutes() {
		for(int i=0; i<routesInSolution.size(); i++)
			if(routesInSolution.get(i).getNodesNumber() == 2)
				routesInSolution.remove(i);
	}
	
	public int getScore() {
		double score = 0;
		for(int i=0; i<routesInSolution.size(); i++)
			score += routesInSolution.get(i).getScore();
		return (int) score;
	}
	
	public ArrayList<Route> getRoutesInSolution() {
		return routesInSolution;
	}
	
	public int getRoutesNumber() {
		return routesInSolution.size();
	}
	
	public double getAvgNodesInRoute() {
		int sum = 0;
		for(int i=0; i<routesInSolution.size(); i++)
			sum += routesInSolution.get(i).getNodesNumber()-2;
		return 1f*sum/routesInSolution.size();
	}
	
	public double getAvgTruckLoad() {
		int sum = 0;
		for(int i=0; i<routesInSolution.size(); i++)
			sum += routesInSolution.get(i).getFinalTruckLoad();
		return 1f*sum/routesInSolution.size();
	}

}
