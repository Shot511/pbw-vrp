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
	
	//done
	public void addRoute(int routeId, int startTime, int boxesNo) {
		if(this.routeIds.contains(routeId)) {
			int zeroOrOne = this.routeIds.indexOf(routeId);
			this.startingTime.set(zeroOrOne, startTime);
		} else {
			this.routeIds.add(routeId);
			this.startingTime.add(startTime);
			this.deliveredInRoute.add(boxesNo);
			this.boxesDelivered += boxesNo;
		}
		
	}
	
	//do not touch, it probably works
	public int getMinPossibleFinishTimeAfter(int routeId, int earliestStartTime, int boxesToDeliver) {
		int earliestEndTime = earliestStartTime+serviceTime*boxesToDeliver;
		if(earliestEndTime>hourEnd)
			return -500; //or other random code meaning failure (it's not C#, can't return null :P )
		if(earliestStartTime<hourStart)
			earliestStartTime = hourStart;
		
		//if it's the first route in this node (done)
		if(routeIds.size()==0 || (routeIds.size()==1 && routeIds.get(0) == routeId)) {
			//we deliver all
			if(boxesToDeliver==boxNoRequest)
				return earliestEndTime;
			//we deliver some and check if future delivery can we handled after || before the current delivery inside time window
			else if(earliestStartTime+serviceTime*boxNoRequest<=hourEnd || hourStart+serviceTime*(boxNoRequest-boxesToDeliver)<=earliestStartTime)
				return earliestEndTime;
			//we deliver some and we need to start delivery after the future delivery ('startingTime' is too early, would block the whole window)
			else
				return hourStart+serviceTime*boxNoRequest;
		}
		//if it's not the first route in this node - so we deliver only some
		else {
			int otherRouteIndex = 1-routeIds.indexOf(routeId); //0 or 1
			//we can manage before the 2nd delivery
			if(earliestEndTime <= startingTime.get(otherRouteIndex)) {
				//and we're not further than 3h before
				if(startingTime.get(otherRouteIndex)-earliestEndTime<=180)
					return earliestEndTime;
				else
					return startingTime.get(otherRouteIndex)-180;
			}
			//we manage after the 2nd delivery
			else if (startingTime.get(otherRouteIndex)+serviceTime*boxesDelivered<=earliestStartTime) {
				//and we're not further than 3h after
				if(earliestStartTime-(startingTime.get(otherRouteIndex)+serviceTime*boxesDelivered)<180)
					return earliestEndTime;
				else
					return -500;
			}
			//starting at 'startingTime' would cause this delivery to cover with the 2nd delivery - start just after the 2nd delivery
			else
				return startingTime.get(otherRouteIndex)+serviceTime*boxNoRequest;
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
