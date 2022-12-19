package SMTravelSimulation;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.SequelActivity;
/*
 * Sequel Activity InputMemberNumber, represents the Call entity entering the member number.
 */
public class InputMemberNumber extends SequelActivity {
	
	static SMTravel model;	// Access to the SMTravel model
	private Call icCall;
	    
	// Constructor
	InputMemberNumber(Call icCall) {
		this.icCall = icCall;
	}
	    
	public void startingEvent() {
		// No starting event
	}
	   
	protected double duration() {
		return rvpuIMNDuration();
	}
	
	protected void terminatingEvent() {
	  	EstimateWaitTime estWTime = new EstimateWaitTime(icCall);
	  	model.spStart(estWTime);       
	}

/*------------------------------------- Embedded RVPs ------------------------------------*/		
	static void initRvps(Seeds sd){
		dmIMNDuration = new Uniform(
				TYPING_TIME[MIN],
				TYPING_TIME[MAX],
				new MersenneTwister(sd.typingTime));
	}   
	private static Uniform dmIMNDuration;
    protected final static double[] TYPING_TIME = {7.0/60.0, 16.0/60.0};
    protected final static int MIN = 0;
	protected final static int MAX = 1;
	// RVP for Input Member Number activity duration
    double rvpuIMNDuration() {
        return dmIMNDuration.nextDouble();
    }
}