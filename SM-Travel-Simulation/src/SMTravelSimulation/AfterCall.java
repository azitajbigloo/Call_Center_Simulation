package SMTravelSimulation;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.SequelActivity;
/*
 * Sequel Activity AfterCall, represents the operator doing the after call work.
 */
public class AfterCall extends SequelActivity{
		
	static SMTravel model;	// Access to the SMTravel model
	private int operatorId;
	private int uCaType;
	   
	// Constructor
	AfterCall(int uCaType, int operatorId) {
		this.operatorId = operatorId;
		this.uCaType = uCaType;
	}

	public void startingEvent(){
	    //No starting event
		//System.out.print("####### After Call - startingEvent ####### \n");
	}
	
	protected double duration(){
		return rvpuAftCallWrkTm(uCaType, operatorId);  
	}
		   
	protected void terminatingEvent(){
		//System.out.print("####### After Call - terminatingEvent ####### \n");
		model.rgOperator[operatorId].numBusy--;        
	}
	
//------------------------------------- Embedded RVPs ------------------------------------//
	static void initRvps(Seeds sd){
		dmAftCallWrkTm = new Uniform[3];
		for (int i = 0; i < 3; i++){
			dmAftCallWrkTm[i] = new Uniform(
					AFTER_CALL_TIME[i][MIN],
	                AFTER_CALL_TIME[i][MAX],
	                new MersenneTwister(sd.afterCallTime[i]));
	    }
	 }
	 private static Uniform[] dmAftCallWrkTm;
	 protected final static int MIN = 0;
	 protected final static int MAX = 1;
	 protected final static double[][] AFTER_CALL_TIME = {
	            {0.05, 0.10}, //INFORMATION
	            {0.5,  0.8 }, //RESERVATION
	            {0.4,  0.6 }  //CHANGE
	    };
	 
	 double rvpuAftCallWrkTm(int caType, int operatorType) {
		 double afterCallTime = dmAftCallWrkTm[caType].nextDouble();
		 if (operatorType == Constants.SILVER) 
			 afterCallTime *= RVPs.SILVER_OPERATOR_REDUCTION;       
		 else if (operatorType == Constants.GOLD)
			 afterCallTime *=RVPs.GOLD_OPERATOR_REDUCTION ;
		 return afterCallTime;
	 }   
}