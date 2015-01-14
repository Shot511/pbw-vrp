package controller;

import java.util.ArrayList;

import model.Node;
import model.Solution;

public class AlgorithmController {

	public static int NODES_NO = 80;
	public static int TRUCK_CAPACITY = 54;
	
	public static ArrayList<Node> allNodes = new ArrayList<Node>();
	public static int MIN_ROUTES_NO;
	
	private Solution currentSolution;
	private Solution bestSolution;
	
	public AlgorithmController() {
		//prepare unchangable list of all nodes and changable estimated number of routes/trucks
		setNodes();
		setMinRoutesNo();
		System.out.println("estimated "+this.MIN_ROUTES_NO+" routes");
		
		currentSolution = new Solution();
		bestSolution = new Solution(currentSolution); //use copying constructor; to be changed, deep copy needed
		for(int i=0;i<1000;i++) {
			currentSolution = new Solution(currentSolution, true);
			if(currentSolution.getScore() < bestSolution.getScore())
				bestSolution = new Solution(currentSolution);
		}
//		1 Construct a feasible solution x; set x*:= x
//		2 Repeat
	//		3 Choose a destroy neighborhood N- and a repair neighborhood
	//		N+ using roulette wheel selection based on previously obtained scores {PIj}
	//		4 Generate a new solution x0 from x using the heuristics
	//		corresponding to the chosen destroy and repair neighborhoods
	//		5 If x0 can be accepted then set x := x0
	//		6 Update scores PIj of N- and N+
	//		7 If f(x) < f(x*) set x* := x
//		8 Until stop criteria is met
//		9 Return x*

	}
	
	//to do: set additional nodes options here
	public static void setNodes() {
		for(int i=0;i<NODES_NO;i++)
			allNodes.add(new Node(i+1));
	}
	
	public static void setMinRoutesNo() {
		int sum = 0;
		for(int i=0; i<allNodes.size(); i++)
			sum += allNodes.get(i).getBoxNoRequest();
		MIN_ROUTES_NO = (int) Math.ceil(1.0*sum/TRUCK_CAPACITY);
	}
}
