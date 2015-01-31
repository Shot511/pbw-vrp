package model;

import java.util.ArrayList;
import java.util.Collections;

import view.MainFrame;

import controller.AlgorithmController;

public class Route {

	private int id;
	private int score;
	private int currentBoxLoad = MainFrame.INSTANCE.getTruckCapacity();
	private int currentWorktime = 0;
	private int maxBoxLoad = MainFrame.INSTANCE.getTruckCapacity();
	private int maxWorktime = 24;
	private double TRUCK_VELOCITY = 50;
	private int globalServiceTime = MainFrame.INSTANCE.getServiceTime();

	private Depot depot = AlgorithmController.depot;
	ArrayList<Node> nodesOnRoute = new ArrayList<Node>();
	ArrayList<Integer> startTimes = new ArrayList<Integer>();
	ArrayList<Integer> boxesDelivered = new ArrayList<Integer>();
	
	Route(int id) {
		this.id = id;
		nodesOnRoute = new ArrayList<Node>();
		nodesOnRoute.add(depot); //depot as a starting point
		nodesOnRoute.add(depot); //and the final point
		startTimes.add(0);
		startTimes.add(0);
		boxesDelivered.add(0);
		boxesDelivered.add(0);
	}
	
	//copying constructor
	Route(Route copyThis) {
		this.id = copyThis.id;
		this.score = copyThis.score;
		this.currentBoxLoad = copyThis.currentBoxLoad;
		this.currentWorktime = copyThis.currentWorktime;
		this.maxBoxLoad = copyThis.maxBoxLoad;
		this.maxWorktime = copyThis.maxWorktime;
		this.id = copyThis.id;
		for(int i=0; i<copyThis.nodesOnRoute.size(); i++) {
			this.nodesOnRoute.add(new Node(copyThis.nodesOnRoute.get(i)));
			this.startTimes.add(copyThis.startTimes.get(i));
			this.boxesDelivered.add(copyThis.boxesDelivered.get(i));
		}
	}

	/**
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

	public void removeNode(Node n) {
		int index = nodesOnRoute.indexOf(n);
		currentBoxLoad += n.getBoxesNoDeliveredByThisTruck(id);
		this.nodesOnRoute.remove(index);
		this.startTimes.remove(index);
		this.boxesDelivered.remove(index);
		addToRoute(null, 0); //does not add anything but updates the route after removing node n
	}
	
	/**
	 * Returns true if route is doable after adding new node. Considers:
		all nodes time windows
		other routes serving those nodes - 3h max distance in delivery
		also if we don't deliver the whole request and we're the first on this node, the time of delivery
			on this node has to be settled so that the other route have time to serve it (consider the number of boxes
			left to be delivered)
	*/
	private boolean addToRoute(Node newNode, int boxesToDeliver) {
		ArrayList<Integer> startTimes = new ArrayList<Integer>(nodesOnRoute.size());
		ArrayList<Integer> boxes = new ArrayList<Integer>(nodesOnRoute.size());
		startTimes.add(0, 0); //random starting time for the depot
		boxes.add(0, 0);
		
		int score = 0;
		int minTime = nodesOnRoute.get(1).getHourStart();
		//check if route with such nodes is doable
		for(int i=1; i<nodesOnRoute.size()-1; i++) {
			Node n = nodesOnRoute.get(i);
			if(n.equals(newNode)) {
				boxes.add(boxesToDeliver);
				minTime = n.getMinPossibleFinishTimeAfter(id, minTime, boxesToDeliver);
				startTimes.add(i, minTime-n.getServiceTime()*boxesToDeliver);
			} else {
				boxes.add(n.getBoxesNoDeliveredByThisTruck(id));
				minTime = n.getMinPossibleFinishTimeAfter(id, minTime, n.getBoxesNoDeliveredByThisTruck(id));
				startTimes.add(i, minTime-n.getServiceTime()*n.getBoxesNoDeliveredByThisTruck(id));
			}
			if(minTime==-500)
				return false;
			//time to get to next node
			minTime += 60*getNodesDistance(n, nodesOnRoute.get(i+1))/TRUCK_VELOCITY;
			score += getNodesDistance(n, nodesOnRoute.get(i+1));
		}
		//set depot times during start and return, and fake 0 as the boxes number for depot delivery
		startTimes.add(nodesOnRoute.size()-1, minTime);
		boxes.add(nodesOnRoute.size()-1, 0);
		startTimes.set(0, (int) (startTimes.get(1)-60*getNodesDistance(nodesOnRoute.get(0),nodesOnRoute.get(1))/TRUCK_VELOCITY));
		if(minTime-startTimes.get(0)>maxWorktime*60)
			return false;
		
		//route is doable - update nodes
		
		for(int i=1; i<nodesOnRoute.size()-1; i++) {
			Node n = nodesOnRoute.get(i);
			if(n.equals(newNode))
				n.addRoute(id, startTimes.get(i), boxesToDeliver);
			else
				n.addRoute(id, startTimes.get(i), n.getBoxesNoDeliveredByThisTruck(id));
		}
		this.startTimes = (ArrayList<Integer>) startTimes.clone();
		this.boxesDelivered = (ArrayList<Integer>) boxes.clone();
		this.currentBoxLoad -= boxesToDeliver;
		this.score = score;
		return true;
	}

	//that works - just kiddin, it doesn't
//	private int getBestRoutePositionIndex(Node n) {
//		//find closest node in this route
//		double minDistance = getNodesDistance(n, nodesOnRoute.get(0));
//		int index = 0;
//		for(int i=1; i<nodesOnRoute.size()-1; i++) {
//			double currentDistance = getNodesDistance(n, nodesOnRoute.get(i));
//			if(currentDistance<minDistance) {
//				minDistance = currentDistance;
//				index = i;
//			}
//		}
//		
//		//check if it's better to put new node between closest-1 and closest or closest and closest+1
//		int indexBefore = index-1;
//		int indexAfter = index+1;
//		if(index==0) indexBefore = nodesOnRoute.size()-2;
//		if(minDistance+getNodesDistance(n, nodesOnRoute.get(indexBefore))-getNodesDistance(nodesOnRoute.get(indexBefore), nodesOnRoute.get(index))
//				<minDistance+getNodesDistance(n, nodesOnRoute.get(indexAfter))-getNodesDistance(nodesOnRoute.get(indexAfter), nodesOnRoute.get(index)))
//			return indexBefore+1; //covers depot case
//		else
//			return indexAfter;
//	}

	private int getBestRoutePositionIndex(Node n) {
		double minDeltaDistance = getNodesDistance(n, nodesOnRoute.get(0))
				+getNodesDistance(n, nodesOnRoute.get(1))
				-getNodesDistance(nodesOnRoute.get(0), nodesOnRoute.get(1));
		int index = 1;
		for(int i=1; i<nodesOnRoute.size()-1; i++) {
			double currentDeltaDistance = getNodesDistance(n, nodesOnRoute.get(i))
					+ getNodesDistance(n, nodesOnRoute.get(i+1))
					- getNodesDistance(nodesOnRoute.get(i), nodesOnRoute.get(i+1));
			if(currentDeltaDistance<minDeltaDistance) {
				minDeltaDistance = currentDeltaDistance;
				index = i+1;
			}
		}
		return index;
	}
	
	public int getScoreDeltaAfterAdding(Node n) {
		int index = getBestRoutePositionIndex(n);
		int distanceAdded = (int) (getNodesDistance(n, nodesOnRoute.get(index))
		+ getNodesDistance(n, nodesOnRoute.get(index-1))
		- getNodesDistance(nodesOnRoute.get(index),nodesOnRoute.get(index-1)));
		
		return distanceAdded;
	}
	
	private double getNodesDistance(Node n1, Node n2) {
		return Math.sqrt(Math.pow(n1.getPositionX() - n2.getPositionX(), 2) + Math.pow(n1.getPositionY() - n2.getPositionY(), 2));
	}
	
	public int getScore() {
		return score;
	}
	
	public ArrayList<Node> getNodesOnRoute() {
		return nodesOnRoute;
	}
	
	public int getNodesNumber() {
		return nodesOnRoute.size();
	}
	
	public int getId() {
		return id;
	}
	
	public int getFinalTruckLoad() {
		return maxBoxLoad-currentBoxLoad;
	}
	
	public ArrayList<Integer> getStartHours() {
		ArrayList<Integer> hours = new ArrayList<Integer>();
		for(int i=0; i<startTimes.size(); i++) {
			if(startTimes.get(i)<0)
				hours.add(24+(startTimes.get(i)-59)/60);
			else
				hours.add(startTimes.get(i)/60);
		}
		return hours;//startTimes;
	}
	
	public ArrayList<Integer> getStartMins() {
		ArrayList<Integer> mins = new ArrayList<Integer>();
		for(int i=0; i<startTimes.size(); i++) {
			if(startTimes.get(i)<0)
				mins.add(startTimes.get(i)%60==0? startTimes.get(i)%60 : 60+startTimes.get(i)%60);
			else
				mins.add(startTimes.get(i)%60);
		}
		return mins;
	}
	
	public ArrayList<Integer> getEndHours() {
		ArrayList<Integer> hours = new ArrayList<Integer>();
		ArrayList<Integer> endTimes = new ArrayList<Integer>();
		for(int i=0; i<startTimes.size(); i++)
			endTimes.add(startTimes.get(i)+globalServiceTime*boxesDelivered.get(i));
		for(int i=0; i<endTimes.size(); i++) {
			if(endTimes.get(i)<0)
				hours.add(24+(endTimes.get(i)-59)/60);
			else
				hours.add(endTimes.get(i)/60);
		}
		return hours;//startTimes;
	}
	
	public ArrayList<Integer> getEndMins() {
		ArrayList<Integer> mins = new ArrayList<Integer>();
		ArrayList<Integer> endTimes = new ArrayList<Integer>();
		for(int i=0; i<startTimes.size(); i++)
			endTimes.add(startTimes.get(i)+globalServiceTime*boxesDelivered.get(i));
		for(int i=0; i<endTimes.size(); i++) {
			if(endTimes.get(i)<0)
				mins.add(endTimes.get(i)%60==0? endTimes.get(i)%60 : 60+endTimes.get(i)%60);
			else
				mins.add(endTimes.get(i)%60);
		}
		return mins;
	}

	public ArrayList<Integer> getBoxesDelivered() {
		return boxesDelivered;
	}
	
}
