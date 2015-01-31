package model;

import java.util.ArrayList;

import view.MainFrame;

public class Node {

	private int id = 1;
	
	protected int positionX;
	protected int positionY;
	
	//do podrasowania. albo nie, nie ma czasu
	private int hourStart = MainFrame.INSTANCE.getHourStart(); //-60; //23:00
	private int hourEnd = MainFrame.INSTANCE.getHourEnd(); //6*60; //6:00
	                                           
	private int serviceTime = MainFrame.INSTANCE.getServiceTime(); //4; //in minutes
	private int boxNoRequest = MainFrame.INSTANCE.getBoxesPerStore(); //15;
	private int boxesDelivered = 0;            
	
	private ArrayList<Integer> routeIds;// = new ArrayList<Integer>(); //remember routes which served us
	private ArrayList<Integer> deliveredInRoute; //remember how many boxes they delivered
	private ArrayList<Integer> startingTimes; //remember when they started
	
	public Node() {
		resetNode();
	}
	
	public Node(int id) {
		this.id = id;
		resetNode();
	}
	
	//copying constructor
	public Node(Node copyThis) {
		resetNode();
		this.id = copyThis.id;
		this.positionX = copyThis.positionX;
		this.positionY = copyThis.positionY;
		this.hourStart = copyThis.hourStart;
		this.hourEnd = copyThis.hourEnd;
		this.serviceTime = copyThis.serviceTime;
		this.boxNoRequest = copyThis.boxNoRequest;
		this.boxesDelivered = copyThis.boxesDelivered;
		for(int i=0; i<copyThis.getRouteIds().size(); i++) {
			this.routeIds.add(copyThis.getRouteIds().get(i));
			this.deliveredInRoute.add(copyThis.getDeliveredInRoute().get(i));
			this.startingTimes.add(copyThis.getStartingTimes().get(i));
		}
	}

	public void resetNode() {
		deliveredInRoute = new ArrayList<Integer>();
		startingTimes = new ArrayList<Integer>();
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
			this.startingTimes.set(zeroOrOne, startTime);
		} else {
			this.routeIds.add(routeId);
			this.startingTimes.add(startTime);
			this.deliveredInRoute.add(boxesNo);
			this.boxesDelivered += boxesNo;
		}
		
	}
	
	//do not touch, it probably works
	public int getMinPossibleFinishTimeAfter(int routeId, int earliestStartTime, int boxesToDeliver) {
		if(earliestStartTime<hourStart)
			earliestStartTime = hourStart;
		int earliestEndTime = earliestStartTime+serviceTime*boxesToDeliver;
		if(earliestEndTime>hourEnd)
			return -500; //or other random code meaning failure (it's not C#, can't return null :P )
		
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
			int otherRouteIndex = routeIds.indexOf(routeId) == -1 ? 0 : 1-routeIds.indexOf(routeId);
			//int otherRouteIndex = 1-routeIds.indexOf(routeId); //0 or 1
			//we can manage before the 2nd delivery
			if(earliestEndTime <= startingTimes.get(otherRouteIndex)) {
				//and we're not further than 3h before
				if(startingTimes.get(otherRouteIndex)-earliestEndTime<=180)
					return earliestEndTime;
				else
					return startingTimes.get(otherRouteIndex)-180;
			}
			//we manage after the 2nd delivery
			else if (startingTimes.get(otherRouteIndex)+serviceTime*boxesDelivered<=earliestStartTime) {
				//and we're not further than 3h after
				if(earliestStartTime-(startingTimes.get(otherRouteIndex)+serviceTime*boxesDelivered)<180)
					return earliestEndTime;
				else
					return -500;
			}
			//starting at 'startingTime' would cause this delivery to cover with the 2nd delivery - start just after the 2nd delivery
			else
				return startingTimes.get(otherRouteIndex)+serviceTime*boxNoRequest;
		}
	}
	
	public int getBoxesNoDeliveredByThisTruck(int routeId) {
		for(int i=0;i<routeIds.size();i++)
			if(routeIds.get(i) == routeId)
				return deliveredInRoute.get(i);
		return 0;
	}
	
	public ArrayList<Integer> getRouteIds() {
		return routeIds;
	}

	public void setRouteIds(ArrayList<Integer> routeIds) {
		this.routeIds = routeIds;
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

	public ArrayList<Integer> getStartingTimes() {
		return startingTimes;
	}

}
