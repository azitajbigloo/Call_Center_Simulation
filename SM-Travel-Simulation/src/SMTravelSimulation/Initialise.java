package SMTravelSimulation;
import simulationModelling.ScheduledAction;
/*
 * Scheduled Action Initialise 
 */
public class Initialise extends ScheduledAction{

	static SMTravel model;
	double [] ts = { 0.0, -1.0 };    // -1.0 ends scheduling
	int tsix = 0;     // set index to first entry
	
	protected double timeSequence() {
		return ts[tsix++];    //only invoked at t=0
	}
		
	public void actionEvent() {
			
		/** HashList and ArrayList are created empty, No need to initialize the group and queues.*/
		//model.qCallLines[Constants.GOLD].callLine.clear();
		//model.qCallLines[Constants.SILVER].callLine.clear();
		//model.qCallLines[Constants.REGULAR].callLine.clear();    
		//model.rgOperator[Constants.GOLD].group.clear();
		//model.rgOperator[Constants.SILVER].group.clear();
		//model.rgOperator[Constants.REGULAR].group.clear();
		//model.rgTrunkLine.list.clear();	
		model.rgTrunkLine.n = 0;  // GAComment: you will need to preserve this - the list attribute is removed from the rqTrunkLine.
		
		// Initialize the output variables
		model.output.ssov_numGoldCalls = 0;
		model.output.ssov_numSilverCalls = 0;
		model.output.ssov_numRegularCalls  = 0;
		model.output.ssov_num90SecGoldCalls = 0;
		model.output.ssov_num180SecSilverCalls = 0;
		model.output.ssov_num900SecRegularCalls = 0;
		model.output.ssov_numBusyCardHCalls = 0;
		model.output.ssov_numBusyRegularCalls = 0;
		model.output.ssov_numRegularArrivalCalls = 0;
		model.output.ssov_numCardHArrivalCalls = 0;  
	}

}