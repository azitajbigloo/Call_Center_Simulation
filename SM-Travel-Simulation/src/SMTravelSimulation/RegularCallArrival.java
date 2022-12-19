package SMTravelSimulation;
import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;
/*
 * Scheduled Action RegularCallArrival 
 */
public class RegularCallArrival extends ScheduledAction {
	
	static SMTravel model;  // Access to the SMTravel model
	
	@Override
	public double timeSequence(){
		return rvpDuCallReg();  
	}

	@Override
	protected void actionEvent() {	
		// Arrival Action Sequence SCS
	  	Call icCall = new Call();
	  	icCall.uCuType = Constants.REGULAR;   // Set Customer type to REGULAR
	  	icCall.uCaType = model.rvp.uCaType(); 
	    	
	 	//If there is an available trunk line add the Call
	 	if(model.rgTrunkLine.n < (model.rgTrunkLine.numLines - model.rgTrunkLine.numReserved)) {	 	
	 		model.rgTrunkLine.n++;	 
	 		EstimateWaitTime estWTime = new EstimateWaitTime(icCall); // Initiate next event
	 		model.spStart(estWTime); 
	  	}
	 	else {
	   	// Call receives busy signal and leaves.
	  	model.udp.UpdateNumArrivalsOutput(icCall);   // update arrivals output
	  	model.udp.UpdateNumBusyOutput(icCall);       // update busy calls output		
	  	}
	}
	    
//------------------------------------- Embedded RVPs ------------------------------------//	    
	static void initRvps(Seeds sd) {
		// Initialize Internal modules, user modules and input variables
	  	time =  (int)(model.getClock() / 60);
		dmCallReg = new Exponential(REGULAR_INTERARR_MEAN[time],new MersenneTwister(sd.arrRegular));	
	}
	// RVP for interarrival times.
	static private int time;
	static private Exponential dmCallReg;
	protected final static double[] REGULAR_INTERARR_MEAN = {
			0.690, //60.0/87.0,   7 AM -  8 AM  
		    0.364, //60.0/165.0,  8 AM -  9 AM
		    0.254, //60.0/236.0,  9 AM - 10 AM
		    0.186, //60.0/323.0,  10 AM - 11 AM
		    0.217, //60.0/277.0,  11 AM - 12 PM
		    0.136, //60.0/440.0,  12 PM -  1 PM
		    0.223, //60.0/269.0,  1 PM -  2 PM
		    0.175, //60.0/342.0,  2 PM -  3 PM
		    0.343, //60.0/175.0,  3 PM -  4 PM
		    0.220, //60.0/273.0,  4 PM -  5 PM
		    0.522, //60.0/115.0,  5 PM -  6 PM
		    1.071, //60.0/56.0,   6 PM -  7 PM
	};
	static protected double rvpDuCallReg(){
		return model.getClock() + dmCallReg.nextDouble(1.0/REGULAR_INTERARR_MEAN[time]);
	}
}	