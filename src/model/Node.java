package model;

import java.util.ArrayList;

public class Node {

	private int id = 1;
	
	protected int positionX;
	protected int positionY;
	
	//do podrasowania. albo nie, nie ma czasu
	private int hourStart = -60; //23:00
	private int hourEnd = 6*60; //6:00
	
	private int serviceTime = 4; //in minutes
	private int boxNoRequest = 15;
	private int boxesDelivered = 0;
	
	private ArrayList<Integer> routeIds; //remember routes which served us
	private ArrayList<Integer> deliveredInRoute; //remember how many boxes they delivered
	private ArrayList<Integer> startingTime; //remember when they started
	
	public Node() {}
	
	public Node(int id) {
		this.id = id;
		this.positionX = (int) (Math.random()*500);
		this.positionY = (int) (Math.random()*500);
		resetNode();
	}
	
	public void resetNode() {
		deliveredInRoute = new ArrayList<Integer>();
		startingTime = new ArrayList<Integer>();
		routeIds = new ArrayList<Integer>();
		boxesDelivered = 0;
	}
	
	public boolean isCovered() {
		if(boxesDelivered==boxNoRequest)
			return true;
		return false;
	}
	
	//to do or not needed
	public int getMinTimeForRoute(int routeId) {
		if(routeIds.size()==0)
			return hourStart;
		else if(routeIds.size()==1 && routeIds.get(0) == routeId)
			return hourStart;
		else {
			if(startingTime.get(0) - serviceTime*(boxNoRequest-boxesDelivered)>=hourStart)
				return hourStart;
			else
				return startingTime.get(0)+boxesDelivered*serviceTime;
		}
	}
	
	//to do:
	public int getMinPossibleFinishTimeAfter(int routeId, int time, int boxesToDeliver) {
		if(time+serviceTime*boxesToDeliver>hourEnd)
			return -500; //or other random code meaning failure (it's not C#, can't return null :P )
		if(routeIds.size()==0 || (routeIds.size()==1 && routeIds.get(0) == routeId)) {
			return time+serviceTime*boxesToDeliver;
		} else {//TO DO!
			if(startingTime.get(0) - serviceTime*(boxNoRequest-boxesDelivered)>=hourStart)
				return hourStart;
			else
				return startingTime.get(0)+boxesDelivered*serviceTime;
		}
	}
	
	public int getBoxesNoDeliveredByThisTruck(int routeId) {
		for(int i=0;i<routeIds.size();i++)
			if(routeIds.get(i) == routeId)
				return deliveredInRoute.get(i);
		return 0;
	}
	
	public ArrayList<Integer> getRouteId() {
		return routeIds;
	}

	public void setRouteId(ArrayList<Integer> routeId) {
		this.routeIds = routeId;
	}

	public int getHourStart() {
		return hourStart;
	}

	public int getBoxesDelivered() {
		return boxesDelivered;
	}

	public void setBoxesDelivered(int boxesDelivered) {
		this.boxesDelivered = boxesDelivered;
	}

	public ArrayList<Integer> getDeliveredInRoute() {
		return deliveredInRoute;
	}

	public void setBoxesDelivered(ArrayList<Integer> deliveredInRoute) {
		this.deliveredInRoute = deliveredInRoute;
	}

	public void setHourStart(int hourStart) {
		this.hourStart = hourStart;
	}

	public int getHourEnd() {
		return hourEnd;
	}

	public void setHourEnd(int hourEnd) {
		this.hourEnd = hourEnd;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getBoxNoRequest() {
		return boxNoRequest;
	}

	public void setBoxNoRequest(int boxNoRequest) {
		this.boxNoRequest = boxNoRequest;
	}

}
