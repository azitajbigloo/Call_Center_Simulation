package SMTravelSimulation;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ExtSequelActivity;
/*
 * Extended Sequel Activity EstimateWaitTime
 */
public class EstimateWaitTime extends ExtSequelActivity{
	
	static SMTravel model;
	private Call icCall;
	
	// Constructor
	EstimateWaitTime(Call icCall){
		this.icCall = icCall;	
	}
	
	public void startingEvent() {
		icCall.startWaitTime = model.getClock();		
	}
	
	protected double duration() {
		return Constants.EWT_VOICE_DURATION;
	}
	
	public int interruptionPreCond(){  
		int interruptID = 0;
		if (model.qCallLines[icCall.uCuType].spIsEmpty() && udpCheckAvailableOperator(icCall.uCuType)) {
			interruptID = 1;
		}
		return interruptID;
	}
	
	public void interruptionSCS(int interruptID){
		if (interruptID == 1) {
			model.qCallLines[icCall.uCuType].spInsertQue(icCall);
			//model.spTerminate();
		}	
	}
	
	protected void terminatingEvent() {
		// call leaves if the customer tolerate wait time is less that calculate expected wait time 
		if(rvpuWaitTmTolerance(icCall.uCuType) < udpCalculateExpectedWaitTime(icCall.uCuType)) {
			model.rgTrunkLine.n--;
			model.output.numHangUp++; // just for debugging
			model.udp.UpdateNumArrivalsOutput(icCall);
		}
		else {
			model.qCallLines[icCall.uCuType].spInsertQue(icCall);
		}	
	}

/*------------------------------------   Embedded UDPs   ----------------------------------------------*/
	
	/* Method: udpCalculateExpectedWaitTime
	 * Description: Calculates the estimated wait time of the customer call
	 * Parameter: Customer Type
	 * Return: the estimated wait time
	 */
	public double udpCalculateExpectedWaitTime(int uCuType) { 
		
		icCall.estWaitTime = Constants.NONE; 
		if(uCuType == Constants.REGULAR) {
			icCall.estWaitTime = Constants.EWT_DURATION/60 * (model.qCallLines[Constants.GOLD].getN() 
					+ model.qCallLines[Constants.SILVER].getN() + model.qCallLines[Constants.REGULAR].getN());
		}
		else if(uCuType == Constants.SILVER) {
			icCall.estWaitTime = Constants.EWT_DURATION/60 * (model.qCallLines[Constants.GOLD].getN()
					+ model.qCallLines[Constants.SILVER].getN());
		}
		else if(uCuType == Constants.GOLD) {
			icCall.estWaitTime = Constants.EWT_DURATION/60 * (model.qCallLines[Constants.GOLD].getN());
		}
		if(icCall.estWaitTime == Constants.NONE) {
			System.out.print("Error occured in calculating estimated wait time.");	
		}
		return icCall.estWaitTime;	
	}
	
	/* Method: udpCheckAvailableOperator
	 * Description: Check if the proper operator is available for the call
	 * Parameter: Customer Type
	 * Return: boolean, true if there is an operator available
	 */
	public boolean udpCheckAvailableOperator(int uCuType) {
		boolean retValue = false;
		if(uCuType == Constants.GOLD) {
			if( (model.rgOperator[Constants.GOLD].numBusy < model.rgOperator[Constants.GOLD].uNumOperators) 
				|| (model.rgOperator[Constants.SILVER].numBusy < model.rgOperator[Constants.SILVER].uNumOperators)
				|| (model.rgOperator[Constants.REGULAR].numBusy < model.rgOperator[Constants.REGULAR].uNumOperators))
				retValue = true;
		}
		else if(uCuType == Constants.SILVER) {
			if((model.rgOperator[Constants.SILVER].numBusy < model.rgOperator[Constants.SILVER].uNumOperators)
					|| (model.rgOperator[Constants.REGULAR].numBusy < model.rgOperator[Constants.REGULAR].uNumOperators))
				retValue = true;
		}
		else if(uCuType == Constants.REGULAR) {
			if(model.rgOperator[Constants.REGULAR].numBusy < model.rgOperator[Constants.REGULAR].uNumOperators)
				retValue = true;
		}
		return retValue;
	}
	
/*-------------------------------------------- Embedded RVPs ---------------------------------------------*/	
	
	static void initRvps(Seeds sd) {
		// Initialise Internal modules, user modules and input variables
		dmWaitTmTolerated = new Uniform[2];
		for (int i = 0; i < 2; i++) {
			dmWaitTmTolerated[i] = new Uniform(
					TOLERATED_WAIT_TIME[i][MIN],
					TOLERATED_WAIT_TIME[i][MAX],
					new MersenneTwister(sd.toleratedWaitTime[i]));
		}
	}
	private static Uniform[] dmWaitTmTolerated;
	protected final static int MIN = 0;
	protected final static int MAX = 1;
	protected final static int MEAN = 2;
	protected final static double[][] TOLERATED_WAIT_TIME = {
	           {12.0, 30.0}, //REGULAR
	           {8.0,  17.0}  //CARDHOLDER
	};
	// RVP for customer wait time tolerance
	static protected double rvpuWaitTmTolerance(int cuType) {
		double waitTimeTolerance = 0;
        if(cuType == Constants.REGULAR) 
        	waitTimeTolerance =  dmWaitTmTolerated[Constants.REGULAR].nextDouble();
        else 
        	waitTimeTolerance =  dmWaitTmTolerated[Constants.CARDHOLDER].nextDouble();
		return waitTimeTolerance; 
    }
}