package SMTravelSimulation;
import java.util.ArrayList;
/* 
 * Q.CallLine[3] The Queue entity category with three queues for three types of customer calls   
 */ 
public class CallLine {

	int n; 		//The number of entities in the list, n
	// Implemented the queue using an ArrayList object
	private ArrayList<Call> callLine = new ArrayList<Call>();  	// List of call objects waiting to be served (order: FIFO)
	
	// Get the size of the callLine (number of call objects in the callLine)
	protected int getN() { return(callLine.size()); }
	
	// Insert the call into the queue
	protected void spInsertQue(Call icCall) { callLine.add(icCall); }
	
	// Remove the call from the queue, and return the call
	protected Call spRemoveQue() { 
		Call icCall = null;
		if(callLine.size() != 0) icCall = callLine.remove(0);
		return(icCall);
	}
	
	// Check if the callLine is empty
	protected boolean spIsEmpty() {
		return (callLine.isEmpty()); 
	}
}


