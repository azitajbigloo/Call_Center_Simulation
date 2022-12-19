package SMTravelSimulation;
//import java.util.HashSet;
/* 
 *  RG.Operator[3] Entity Category    
 */
public class Operator {
	
	// Attributes
	int uNumOperators; 	//The total number of operators within a day
	int numBusy; 	     //The assigned value represents the number of operators that are busy
	public int[] schedule;  //The number of operators starting at each shift

	@Override
    public String toString() {
        return ("Operators: numBusy = " + numBusy);
    }
}