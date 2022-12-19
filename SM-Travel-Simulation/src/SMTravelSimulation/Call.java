package SMTravelSimulation;
/*
 * Call Entities Class 
 */
public class Call {
	
	 int uCuType; // Customer call type
	 int uCaType;  // Call type
     double startWaitTime;  // Time the Call arrives 
     double estWaitTime;// calculated wait Time
     
     @Override
     public String toString() {
         return ("Call: uCuType = " + Constants.CALLTYPE[uCuType]
                     + ", uCaType = " + Constants.CALLTYPE[uCaType]
                     + ", estWaitTime = " + estWaitTime
                     + ", startWaitTime = " + startWaitTime);
     }
}