package SMTravelSimulation;
import simulationModelling.ScheduledAction;
/*
 * Scheduled action: StaffCahnge
 */
class StaffChange extends ScheduledAction {
	
	static SMTravel model;  // reference to model object
    static double[] staffChangeTimeSeq = {0,60,120,180,240,480,540,600,660,720,-1};
    // = {0,3600,7200,10800,14400,28800,32400,36000,39600,43200,-1} in seconds
	private int sctIx = 0;
	
	public double timeSequence() { 
		double nxtTime = staffChangeTimeSeq[sctIx];
		sctIx++;
		return(nxtTime); 
	}

	protected void actionEvent() {
		 udpStaffChange();       
	}
	
/*------------------------------------   Embedded UDPs   ----------------------------------------------*/	
	/* Method: udpStaffChange
	 * Description: changes the number of working operators at start of each shift, and remove them at the end of the shifts
	 * Parameter: nothing
	 * Return: nothing
	 */
	protected static void udpStaffChange() {
	    for (int i = 0; i < 9; i++){
	        if (model.getClock() == staffChangeTimeSeq[i] ){
	        	int shift = i % 5;
	        	//if (i == 1) System.out.print("Error");
	            if (i < 5) { //Shift beginning
	            	for (int opType = 0; opType < 3; opType++){
	            		model.rgOperator[opType].uNumOperators += model.rgOperator[opType].schedule[shift];   
	            	}
	            }
	            else { //Shift ending
	               for (int opType = 0; opType < 3; opType++){
	                   model.rgOperator[opType].uNumOperators -= model.rgOperator[opType].schedule[shift];
	               }	
	            }  
	        }
	    }
	}
}
	   