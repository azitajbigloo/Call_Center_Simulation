package SMTravelSimulation;
/*
 * Outputs
 */
public class Output {
	
	static SMTravel model;	
	// SSOVs
	int ssov_numGoldCalls ; 		 //The number of gold calls that call the SM Travel center.
	int ssov_num90SecGoldCalls ; 	 //The number of gold calls that exceeded the 90 second wait time.
	int ssov_numSilverCalls; 		 //The number of silver calls that enter the SM Travel system.
	int ssov_num180SecSilverCalls; 	 //The number of silver calls that exceeded the 180 second wait time.
	int ssov_numRegularCalls; 		 //The number of regular calls that enter the SM Travel system.
	int ssov_num900SecRegularCalls;  //The number of regular calls that exceeded the 900 second wait time.
	
	int ssov_numBusyCardHCalls;  	    //The number of gold/silver calls that receive a busy signal.
	int ssov_numCardHArrivalCalls;      //The number of card hold calls that arrived at the very start of the system.
	int ssov_numBusyRegularCalls; 	    //The number of regular calls that receive a busy signal.
	int ssov_numRegularArrivalCalls;    //The number of regular calls that arrived at the very start of the system.
	int numHangUp; 

	double ssov_perc90SecGoldCalls;      // The percentage of gold calls that exceeded the 90 second wait time.
	double ssov_perc180SecSilverCalls;   //The percentage of silver calls that exceeded the 180 second wait time.
	double ssov_perc900SecRegularCalls;  //The percentage of regular calls that exceeded the 900 second wait time.
	double ssov_percBusyCardHCalls;      //The percentage of card hold calls that receive busy signal.
	double ssov_percBusyRegularCalls;    //The percentage of regular calls that receive a busy signal.
	
	// getter methods for getting the percentages
	public double getPerc900SecRegularCalls() {
		if(ssov_numRegularCalls == 0) 
			ssov_perc900SecRegularCalls = 0.0;
		else  
			ssov_perc900SecRegularCalls = (double)ssov_num900SecRegularCalls/(double)ssov_numRegularCalls;	
		return ssov_perc900SecRegularCalls;
	}
	public double getPerc180SecSilverCalls() {
		if(ssov_numSilverCalls == 0) 
			ssov_perc180SecSilverCalls = 0.0;
		else 
			ssov_perc180SecSilverCalls = (double)ssov_num180SecSilverCalls/(double)ssov_numSilverCalls;
		return ssov_perc180SecSilverCalls;
	}
	public double getPerc90SecGoldCalls() {
		if(ssov_numGoldCalls == 0) 
			ssov_perc90SecGoldCalls = 0.0;
		else 
			ssov_perc90SecGoldCalls = (double)ssov_num90SecGoldCalls/(double)ssov_numGoldCalls;
		return ssov_perc90SecGoldCalls;
	}
	public double getPercBusyRegularCalls() {
		if(ssov_numRegularArrivalCalls == 0) 
			ssov_percBusyRegularCalls = 0.0;
		else 
			ssov_percBusyRegularCalls = (double)ssov_numBusyRegularCalls/(double)ssov_numRegularArrivalCalls;
		return ssov_percBusyRegularCalls;
	}
	public double getPercBusyCardHCalls() {
		if(ssov_numCardHArrivalCalls == 0) 
			ssov_percBusyCardHCalls = 0.0;
		else 
			ssov_percBusyCardHCalls = (double)ssov_numBusyCardHCalls/(double)ssov_numCardHArrivalCalls;
		return ssov_percBusyCardHCalls;
	}
}