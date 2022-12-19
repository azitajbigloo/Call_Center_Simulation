
import SMTravelSimulation.SMTravel;
import SMTravelSimulation.Seeds;
import cern.jet.random.engine.*;
// Main Method: Experiments
class Experiment3 {
    static int [] [] schedules =
	     {
	       { 3, 3, 3, 3, 3},  // Regular 
	       { 0, 0, 0, 0, 0},  // Silver
	       { 0, 0, 0, 0, 0}  // Gold    
	     };
	public static void main(String[] args) {
		
		int t, NUMRUNS = 20; 
	    double startTime=0.0;
	    double endTime=720.0;   
	    Seeds[] sds = new Seeds[NUMRUNS];

	   // RandomSeedGenerator rsg = new RandomSeedGenerator(); //random seed generator
	    RandomSeedGenerator rsg = new RandomSeedGenerator();
  	    for(t=0 ; t<NUMRUNS ; t++) 
  	    	 sds[t] = new Seeds(rsg); 
  	    
  	    // run the simulation for NUMRUNS times
	    for(t=0 ; t < NUMRUNS ; t++) {
	    //	System.out.println("t:"+ t);
	    	int numLines = 250;
	 	    int numReserved = 100;
	 	    
	 	   // SMTravel model base case
	    	SMTravel SMT_Base= new SMTravel(startTime,endTime,schedules,numLines,numReserved, sds[t],false); // Simulation object
	    
		    int numShifts = 5; // number of shifts
		  
		    SMTravel[] SMT_Shift = new SMTravel[numShifts]; // Simulation object, SMTravel model for each shift
	    	int [][] currentSchedules = schedules;
	  	    int[][] newSchedules = new int[3][5];
	  	    // Lets get a set of uncorrelated seeds
	  	  
	  	    int l = 0;
	  	    int j = -1;
	  	    int regularShift = 0;
	  	    int silverShift = 0;
	  	    int goldShift = 0;
	  	    boolean conditionMet = false;
	    	 
		    SMT_Base.runSimulation();
		    SMTravel SMT_CurrentTest = SMT_Base;
	    // Checks for the wait time criteria until the standard of customer service is met
		    while(SMT_CurrentTest.getPerc90SecGoldCalls() > 0.02 || SMT_CurrentTest.getPerc180SecSilverCalls() > 0.05 ||SMT_CurrentTest.getPerc900SecRegularCalls() > 0.15) {
	    	 
	    //	System.out.println("Gold");
			for( int i = 0 ; i<5 ; i++) {
				 l = 0;
			     for( int k = 0 ; k<3 ; k++) {
			    	  if (k == 0) {	 j += 1;  }
			    	  newSchedules = currentSchedules;
			    	  newSchedules[l][j]++;
			    	  SMT_Shift[k] = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved, sds[t],false); 
			    	  SMT_Shift[k].runSimulation();
			    	//  System.out.println(Arrays.deepToString(newSchedules));
			    	  if( SMT_Shift[k].getPerc90SecGoldCalls() < 0.02) {
			    		  conditionMet = true;
			    		  currentSchedules = newSchedules;
			    		  SMT_CurrentTest = SMT_Shift[k];
			    		  break;
					  }
			    	  newSchedules[l][j]--;
			    	  l++;
			     }
			     if (conditionMet) {
			    	// System.out.println("Condition met");
			    	 break;
			     }
			 }
			
		     if (!conditionMet) {
		    	 if (goldShift == 5) {
		    		 goldShift = 0;
		    	 }
		    	 //System.out.println("Condition not met");
		    	 newSchedules[2][goldShift]++;
		    	 goldShift++;
		     }
		     
			conditionMet = false;
			j = -1;

			//System.out.println("Silver");
 	    	for( int i = 0 ; i<5 ; i++) {
	    		 l = 0;
			     for( int k = 0 ; k<2 ; k++) {
			    	  if (k == 0) {
			    		  j += 1;
			    	  }
			    	  newSchedules = currentSchedules;
			    	  newSchedules[l][j]++;
			    	  SMT_Shift[k] = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,sds[t],false); 
			    	  SMT_Shift[k].runSimulation();
			    	 // System.out.println(Arrays.deepToString(newSchedules));
			    	  if( SMT_Shift[k].getPerc180SecSilverCalls() < 0.05) {
			    		  conditionMet = true;
			    		  currentSchedules = newSchedules;
			    		  SMT_CurrentTest = SMT_Shift[k];
			    		  break;
		    		  }
			    	  newSchedules[l][j]--;
			    	  l++;
			     }
			     if (conditionMet) {
			    	 //System.out.println("Condition met");
			    	 break;
			     }
		     }
 	    	
		     if (!conditionMet) {
		    	 if (silverShift == 5) {
		    		 silverShift = 0;
		    	 }
		    	 //System.out.println("Condition not met");
		    	 newSchedules[1][silverShift]++;
		    	 silverShift++;
		     }
			 conditionMet = false;
			 j = -1;
	    	 
	    	// System.out.println("Regular");
 	    	for( int i = 0 ; i<5 ; i++) {
	    		 l = 0;
			     for( int k = 0 ; k<1 ; k++) {
			    	  if (k == 0) {
			    		  j += 1;
			    	  }
			    	  newSchedules = currentSchedules;
			    	  newSchedules[l][j]++;
			    	  SMT_Shift[k] = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved, sds[t],false); 
			    	  SMT_Shift[k].runSimulation();
			    	 // System.out.println(Arrays.deepToString(newSchedules));
			    	  if( SMT_Shift[k].getPerc900SecRegularCalls() < 0.15) {
			    		  conditionMet = true;
			    		  currentSchedules = newSchedules;
			    		  SMT_CurrentTest = SMT_Shift[k];
			    		  break;
		    		  }
			    	  newSchedules[l][j]--;
			    	  l++;
			     }
			     if (conditionMet) {
			    	 //System.out.println("Condition met");
			    	 break;
			     }
		     } 	    	
		     if (!conditionMet) {
		    	 if (regularShift == 5) {
		    		 regularShift = 0;
		    	 }
		    	 //System.out.println("Condition not met");
		    	 newSchedules[0][regularShift]++;
		    	 regularShift++;
		     } 	    	
			 conditionMet = false;
			 j = -1;		    	 
	     }
	     // set the number of trunk lines to minimum, and the schedule to the current schedule obtained above to
	     // find the number of trunk lines needed to meet the standard of customer service.
	     numLines = 50;
		 numReserved = 0;
		 SMT_CurrentTest.runSimulation();
		 // checks the buys criteria until the standard of customer service is met
	     while (!(SMT_CurrentTest.getPercBusyRegularCalls() < 0.02) || !(SMT_CurrentTest.getPercBusyCardHCalls() < 0.2) ){
	    	 SMT_CurrentTest = new SMTravel(startTime,endTime,currentSchedules,numLines,numReserved, sds[t],false);
	    	 SMT_CurrentTest.runSimulation();
	    	 if (SMT_CurrentTest.getPercBusyRegularCalls() > 0.2) {
	    		 numLines += 5;
	    	 }
	    	 if (SMT_CurrentTest.getPercBusyCardHCalls() > 0.02) {
	    		 numReserved += 1; 
	    	 }   
	     }
	     
	   // Calculate total cost
	      int totalRegularOperators = 0;
		  int totalSilverOperators = 0;
		  int totalGoldOperators = 0;
		  
		  for(int r = 0 ; r < 5; r++ ) {
			  totalRegularOperators += currentSchedules[0][r];
			  totalSilverOperators += currentSchedules[1][r];
			  totalGoldOperators += currentSchedules[2][r];
		  }
	  	  double totalCost = 8*(23* totalGoldOperators + 20* totalSilverOperators + 
	  			  			16*totalRegularOperators) + 170* (numLines - 50);
     
     //Printing Solution to Console
     System.out.println("---------------------Solution Obtained----------------------------");
     System.out.println("Perc900SecRegularCalls:"+SMT_CurrentTest.getPerc900SecRegularCalls());
     System.out.println("Perc180SecSilverCalls:"+SMT_CurrentTest.getPerc180SecSilverCalls());
     System.out.println("Perc90SecGoldCalls:"+SMT_CurrentTest.getPerc90SecGoldCalls());
     System.out.println("PercBusyCrdHCalls:"+SMT_CurrentTest.getPercBusyCardHCalls());
     System.out.println("PercBusyRegularCalls:"+SMT_CurrentTest.getPercBusyRegularCalls());
     
     for (int h = 0; h < currentSchedules.length; h++) {
    	 if (h==0)
    		 System.out.println("**Regular Operators: ");
    	 if (h==1)
    		 System.out.println("**Silver Operators: ");
    	 if (h==2)
    		 System.out.println("**Gold Operators: ");
       
    	 for (int g = 0; g< currentSchedules[0].length; g++) {
    		 if (g==0)
    			 System.out.print("7AM: " + currentSchedules[h][g] + ", ");
    		 if (g==1)
    			 System.out.print("8AM: " + currentSchedules[h][g] + ", ");
    		 if (g==2)
    			 System.out.print("9AM: " + currentSchedules[h][g] + ", ");
    		 if (g==3)
    			 System.out.print("10AM: " + currentSchedules[h][g] + ", ");
    		 if (g==4)
    			 System.out.print("11AM: " + currentSchedules[h][g] + " ");
    	 }   
    	 System.out.println("");
     }
     System.out.println("Percentage of Busy Regular Calls: " + SMT_CurrentTest.getPercBusyRegularCalls());
     System.out.println("Percentage of Busy Card Holder Calls: " + SMT_CurrentTest.getPercBusyCardHCalls());
     System.out.println("Number of Trunk Lines: " + numLines);
     System.out.println("Number of Reserved Lines: " + numReserved);
     System.out.println("Total Cost is: $" + totalCost);
	}
	}
}
