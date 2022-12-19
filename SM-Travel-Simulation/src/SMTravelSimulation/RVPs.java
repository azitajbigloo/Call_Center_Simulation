package SMTravelSimulation;
import cern.jet.random.engine.MersenneTwister;
/*
 * RVPs 
 */
public class RVPs {
	static SMTravel model; // for accessing the clock
    
    private MersenneTwister dmCaType;
   
    // Constructor
    RVPs(Seeds sd) {
        dmCaType = new MersenneTwister(sd.callType);
    }
  
    protected final static int MIN = 0;
    protected final static int MAX = 1;
    protected final static int MEAN = 2;
    protected final static double SILVER_OPERATOR_REDUCTION = 0.95; //95% of the time needed by a REGULAR operator
    protected final static double GOLD_OPERATOR_REDUCTION = 0.88;   //88% of the time needed by a REGULAR operator
    protected final static double[] PROPORTION_SUBJECT = {
        0.16, //INFORMATION
        0.76, //RESERVATION
        0.08  //CHANGE
    };  
    
    int uCaType() 
    {
        double randNum = dmCaType.nextDouble();
        int caType;
        if(randNum < PROPORTION_SUBJECT[Constants.INFO])
            caType = Constants.INFO;
        else if (randNum < PROPORTION_SUBJECT[Constants.INFO] + PROPORTION_SUBJECT[Constants.RSRVN]) 
            caType = Constants.RSRVN;
        else 
            caType = Constants.CHNG;
        return caType;
    }
}