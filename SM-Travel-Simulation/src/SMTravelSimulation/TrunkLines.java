package SMTravelSimulation;
/* 
 *  RG.TrunkLine Entity Category    
 */
public class TrunkLines {	
	// Attributes
	int n;    //The number of n being used 
	protected int numLines; 	//The  total number of trunk lines
	protected int numReserved;  //The number of reserved lines
   
	@Override
	public String toString() {
		return ("TrunkLines: n = " + n);
	}
}