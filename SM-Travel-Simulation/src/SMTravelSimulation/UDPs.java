package SMTravelSimulation;
/*
* UDPs
*/
public class UDPs {

	static SMTravel model;  // for accessing the clock
	
	/* Method: UpdateWaitCallsOutput
	 * Description: updates the output variables when the call end waiting 
	 * Parameter: call
	 * Return: nothing
	 */
	protected void UpdateWaitCallsOutput(Call icCall){
		double waiTime = model.getClock() - icCall.startWaitTime;																
		if(icCall.uCuType == Constants.REGULAR){  
			model.output.ssov_numRegularCalls ++;
			if(waiTime > 15.0 )  // 15 minutes = 900 seconds
				model.output.ssov_num900SecRegularCalls ++;
			model.output.getPerc900SecRegularCalls();
		}
		else if(icCall.uCuType==Constants.SILVER){
			model.output.ssov_numSilverCalls ++;
			if(waiTime > 3.0) // 3 minutes = 180 seconds
				model.output.ssov_num180SecSilverCalls++;
			model.output.getPerc180SecSilverCalls();	 
		}
		else if(icCall.uCuType == Constants.GOLD){
			model.output.ssov_numGoldCalls ++;
		 	if(waiTime > 1.5) // 1.5 minutes = 90 seconds
		 		model.output.ssov_num90SecGoldCalls ++;
		 	model.output.getPerc90SecGoldCalls();
		}
	 }
		
	/* Method: UpdateNumArrivalsOutput
	 * Description: update the arrival outputs ( regular and card holder)
	 * Parameter: call
	 * Return: nothing
	 */
	protected void UpdateNumArrivalsOutput(Call icCall) {
		if(icCall.uCuType ==  Constants.REGULAR) {
			model.output.ssov_numRegularArrivalCalls++; 
		}
		else {	
			model.output.ssov_numCardHArrivalCalls++; 
		}
	}
		
	/* Method: UpdateNumBusyOutput
	 * Description: update the number of calls that receive busy signal
	 * Parameter: call
	 * Return: nothing
	 */
	protected void UpdateNumBusyOutput(Call icCall) {
		if(icCall.uCuType ==  Constants.REGULAR) { 
			model.output.ssov_numBusyRegularCalls ++;	
			model.output.getPercBusyRegularCalls();
		}
		else {
			model.output.ssov_numBusyCardHCalls ++;
			model.output.getPercBusyCardHCalls();
		}
	}		
}
