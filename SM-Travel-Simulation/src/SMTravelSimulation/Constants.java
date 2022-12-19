package SMTravelSimulation;
/* 
 * Constants 
 */
public class Constants {

	final static int REGULAR = 0;	 // Identifier for regular operators/calls.   
	final static int SILVER = 1;     // Identifier for silver operators/calls.
	final static int GOLD = 2;	     // Identifier for gold operators/calls.
	final static int CARDHOLDER = 1; // Identifier for Card holder calls
	
	final static double EWT_VOICE_DURATION = 8.0;// Wait time duration for the voice in the phone to estimate the expected wait time
	final static double EWT_DURATION = 240.0; 	 // Pessimistic wait time for a customer to be serviced.

    final static int INFO = 0; 			// Identifier for Information call type
    final static int RSRVN = 1; 		// Identifier for Reservation call type
    final static int CHNG = 2;  		// Identifier for Change call type
   
    // These constants are needed for the SM, they are also added to the Constants table in the CM
    final static String[] CUSTOMERTYPE = {"REGULAR", "SILVER", "GOLD"};	  // Customer Type (Regular, Silver or Gold)
    final static String[] CALLTYPE = {"INFO", "RSRVN" , "CHNG"};  // Call Type (Information, Reserving or Changing)
    final static int NONE = -1;  
    final static int[] STAFF_CHANGE_TIMESEQ = {0,60,120,180,240,480,540,600,660,720,-1}; // Time sequence of shift
}