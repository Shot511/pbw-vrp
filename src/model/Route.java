package model;

import java.util.ArrayList;
import java.util.Collections;

public class Route {

	private int id;
	private int score;
	private int currentBoxLoad = 54;
	private int currentWorktime = 0;
	private int maxBoxLoad = 54;
	private int maxWorktime = 24;
	private double TRUCK_VELOCITY = 50;
	private Depot depot;
	ArrayList<Node> nodesOnRoute;
	
	Route(int id) {
		this.id = id;
		nodesOnRoute = new ArrayList<Node>();
		depot = new Depot();
		nodesOnRoute.add(depot); //depot as a starting point
		nodesOnRoute.add(depot); //and the final point
	}

	/**
	 * 
	 * @param n
	 * @return true if Node was added successfully to the Route, otherwise false
	 */
	public boolean addNode(Node n) {
		if(currentBoxLoad == 0) return false;
		
		int boxesToDeliver = 0;
		
		if(n.getBoxesDelivered()!=0) { //already some boxes were delivered
			if(currentBoxLoad<n.getBoxNoRequest()-n.getBoxesDelivered()) // ...and we can't fill the rest of request
				return false;
			else														// ...and we can (maybe!) fill the rest of request
				boxesToDeliver = n.getBoxNoRequest()-n.getBoxesDelivered();
		} else { //none of the boxes were delivered yet
			if(currentBoxLoad<n.getBoxNoRequest()) //...and we can (maybe!) fill part of the request
				boxesToDeliver = currentBoxLoad;
			else									//...and we can (maybe!) fill the whole request
				boxesToDeliver = n.getBoxNoRequest();
		}
		
		int bestPositionIndex = getBestRoutePositionIndex(n);
		nodesOnRoute.add(bestPositionIndex, n);
		
		
		if(!addToRoute(n, boxesToDeliver)) {
			Collections.reverse(nodesOnRoute);
			if(!addToRoute(n, boxesToDeliver)) {
				Collections.reverse(nodesOnRoute);
				nodesOnRoute.remove(bestPositionIndex);
				return false;
			}
		}
		
		return true;
	}

	/**
	 * to do - in progress - should return true if route is doable after adding new node. Should consider:
		all nodes time windows
		other routes serving those nodes - 3h max distance in delivery
		also if we don't deliver the whole request and we're the first on this node, the time of delivery
			on this node has to be settled so that the other route have time to serve it (consider the number of boxes
			left to be delivered)
	*/
	private boolean addToRoute(Node newNode, int boxesToDeliver) {
		ArrayList<Integer> startTimes = new ArrayList<Integer>(nodesOnRoute.size());
		
		int minTime = nodesOnRoute.get(1).getHourStart();
		//check if route with such nodes is doable
		for(int i=1; i<nodesOnRoute.size()-1; i++) {
			Node n = nodesOnRoute.get(i);
			if(n.equals(newNode)) {
				minTime = n.getMinPossibleFinishTimeAfter(id, minTime, boxesToDeliver);
				startTimes.set(i, minTime-n.getServiceTime()*boxesToDeliver);
			} else {
				minTime = n.getMinPossibleFinishTimeAfter(id, minTime, n.getBoxesNoDeliveredByThisTruck(id));
				startTimes.set(i, minTime-n.getServiceTime()*n.getBoxesNoDeliveredByThisTruck(id));
			}
			if(minTime==-500)
				return false;
			//time to get to next node
			minTime += getNodesDistance(n, nodesOnRoute.get(i+1))/TRUCK_VELOCITY;
		}
		startTimes.set(0, (int) (startTimes.get(1)-getNodesDistance(nodesOnRoute.get(0),nodesOnRoute.get(1))/TRUCK_VELOCITY));
		if(minTime-startTimes.get(0)>maxWorktime*60)
			return false;
		
		//route is doable - update nodes
		int score = 0;
		for(int i=1; i<nodesOnRoute.size()-1; i++) {
			Node n = nodesOnRoute.get(i);
			if(n.equals(newNode))
				n.addRoute(id, startTimes.get(i), boxesToDeliver);
			else
				n.addRoute(id, startTimes.get(i), n.getBoxesNoDeliveredByThisTruck(id));
			
			score += getNodesDistance(n, nodesOnRoute.get(i+1));
		}
		this.currentBoxLoad -= boxesToDeliver;
		this.score = score;
		return true;
	}

	//that works
	private int getBestRoutePositionIndex(Node n) {
		//find closest node in this route
		double minDistance = getNodesDistance(n, nodesOnRoute.get(0));
		int index = 0;
		for(int i=1; i<nodesOnRoute.size()-1; i++) {
			double currentDistance = getNodesDistance(n, nodesOnRoute.get(i));
			if(currentDistance<minDistance) {
				minDistance = currentDistance;
				index = i;
			}
		}
		
		//check if it's better to put new node between closest-1 and closest or closest and closest+1
		int indexBefore = index-1;
		int indexAfter = index+1;
		if(index==0) indexBefore = nodesOnRoute.size()-2;
		if(minDistance+getNodesDistance(n, nodesOnRoute.get(indexBefore))-getNodesDistance(nodesOnRoute.get(indexBefore), nodesOnRoute.get(index))
				<minDistance+getNodesDistance(n, nodesOnRoute.get(indexAfter))-getNodesDistance(nodesOnRoute.get(indexAfter), nodesOnRoute.get(index)))
			return indexBefore+1; //covers depot case
		else
			return indexAfter;
	}
	
	private double getNodesDistance(Node n1, Node n2) {
		return Math.sqrt(Math.pow(n1.getPositionX() - n2.getPositionX(), 2) + Math.pow(n1.getPositionY() - n2.getPositionY(), 2));
	}
	
	public int getScore() {
		return score;
	}
	
}
