package SMTravelSimulation;
import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;
/*
 * Scheduled Action CardHolderCallArrival 
 */
public class CardHolderCallArrival extends ScheduledAction {
	
	static SMTravel model; 	// Access to the SMTravel model

	@Override
	public double timeSequence() {
		return rvpDuCallCrd();
	}
     
	@Override
	protected void actionEvent() {
    	// Arrival Action Sequence SCS
    	Call icCall = new Call();  
    	icCall.uCuType = rvpuCuTypeCrdH();
    	icCall.uCaType = model.rvp.uCaType();
    	
    	//If there is an available trunk line add the Call
    	if(model.rgTrunkLine.n < model.rgTrunkLine.numLines) {
    		model.rgTrunkLine.n++;
    		InputMemberNumber inputMNum = new InputMemberNumber(icCall);
    		model.spStart(inputMNum);    		
    	}	
    	else {
    		// Call receives busy signal and leaves
    		model.udp.UpdateNumArrivalsOutput(icCall);  // update arrivals output
    		model.udp.UpdateNumBusyOutput(icCall);		// update busy calls output
    	}
	}
	
//------------------------------------- Embedded RVPs ------------------------------------//	    
	    
    static void initRvps(Seeds sd) {
    	// Initialise Internal modules, user modules and input variables
    	time = (int)(model.getClock() / 60);
    	dmCallCrd = new Exponential(CARDHOLDER_INTERARR_MEAN[time], new MersenneTwister(sd.arrCardholder));
    	dmCuType = new MersenneTwister(sd.cardholderType);
    }
    static private int time;
	static private Exponential dmCallCrd;
	static private MersenneTwister dmCuType;
		
	protected final static double PROPORTION_SILVER_CARDHOLDER = 0.68;   
	protected final static double[] CARDHOLDER_INTERARR_MEAN = {
			0.674, //60.0/89.0,   7 AM -  8 AM
			0.247, //60.0/243.0,  8 AM -  9 AM
			0.271, //60.0/221.0,  9 AM - 10 AM
			0.333, //60.0/180.0,  10 AM - 11 AM
			0.199, //60.0/301.0,  11 AM - 12 PM
			0.122, //60.0/490.0,  12 PM -  1 PM
			0.152, //60.0/394.0,  1 PM -  2 PM
			0.173, //60.0/347.0,  2 PM -  3 PM
			0.250, //60.0/240.0,  3 PM -  4 PM
			0.223, //60.0/269.0,  4 PM -  5 PM
			0.414, //60.0/145.0,  5 PM -  6 PM
			0.870, //60.0/69.0,   6 PM -  7 PM
	};
	static protected double rvpDuCallCrd() {
		time = (int)(model.getClock() / 60);
		return model.getClock() + dmCallCrd.nextDouble(1.0/CARDHOLDER_INTERARR_MEAN[time]);       
	}
	static protected int rvpuCuTypeCrdH() {  
		double randNum = dmCuType.nextDouble();
		int cuType;
		if(randNum < PROPORTION_SILVER_CARDHOLDER)
			cuType = Constants.SILVER;
		else 
			cuType = Constants.GOLD;
		return cuType;
	}    
}