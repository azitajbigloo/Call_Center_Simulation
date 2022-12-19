import SMTravelSimulation.SMTravel;
import SMTravelSimulation.Seeds;
import cern.jet.random.engine.*;
import outputAnalysis.ConfidenceInterval;

 // Main Method: Experiments
 // 
 class Validation
 {
 /* Result of Experiment:
  7AM: 16, 8AM: 4, 9AM: 4, 10AM: 3, 11AM: 3 
  Silver Operators: 
  7AM: 4,  8AM: 4, 9AM: 4, 10AM: 4, 11AM: 2 
  Gold Operators: 
  7AM: 4,  8AM: 2, 9AM: 3, 10AM: 2, 11AM: 1 
  getPercBusyRegularCalls: 0.0
  getPercBusyCardHCalls: 0.0
  Number of Trunk Lines: 50
  Number of Reserved Lines: 0
  Total Cost is: $8928.0
  * 
  * 
  * Regular Operators: 
7AM: 12 8AM: 3 9AM: 4 10AM: 3 11AM: 4 
Silver Operators: 
7AM: 8 8AM: 1 9AM: 3 10AM: 2 11AM: 5 
Gold Operators: 
7AM: 1 8AM: 1 9AM: 1 10AM: 2 11AM: 2 
Number of Trunk Lines: 50
Number of Reserved Lines: 0
Total Cost is: $7656.0

7 AM: 31, 8 AM: 4, 9 AM: 7, 10 AM: 5, 11 AM: 3 
Silver Operators: 
7 AM: 3, 8 AM: 3, 9 AM: 3, 10 AM: 3, 11 AM: 3 
Gold Operators: 
7 AM: 2, 8 AM: 2, 9 AM: 2, 10 AM: 2, 11 AM: 2 
getPercBusyRegularCalls: 0.0
getPercBusyCardHCalls: 0.0
Number of Trunk Lines: 50
Number of Reserved Lines: 0
Total Cost is: $10640.0
[[23, 4, 4, 4, 3], [3, 3, 3, 3, 3], [2, 2, 2, 2, 2]]
7AM: 31, 8AM: 4, 9AM: 7, 10AM: 5, 11AM: 3 
   Silver Operators: 
7AM: 3, 8AM: 3, 9AM: 3, 10AM: 3, 11AM: 3 
   Gold Operators: 
   
7AM: 13 8AM: 3 9AM: 3 10AM: 4 11AM: 3 
Silver Operators: 
7AM: 3 8AM: 4 9AM: 3 10AM: 3 11AM: 3 
Gold Operators: 
7AM: 2 8AM: 1 9AM: 1 10AM: 1 11AM: 3 
  */
    static int [] [] schedules =
    {
      { 12, 12, 12, 12, 12},  // Regular 
      { 0, 0, 0,0, 0},  // Silver
      { 0, 0, 0, 0, 0}  // Gold    
    };
    public static void main(String[] args)
    {
    	int numLines = 50;
    	int numReserved = 0;
        double startTime=0.0, endTime=720.0; // Declare Observation Interval
        Seeds[] sds;    // Seed Object Declaration
        SMTravel SMT1;
       // SMT2, SMT3, SMT4, SMT5;  // Simulation object

        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();

        // Declaring an array which contains the number of runs - To see if the confidence interval changes over that time
        int[] numRuns = new int[6];
        numRuns[0] = 10;  // 10 Runs
        numRuns[1] = 20;  // 20 Runs
        numRuns[2] = 40;  // 40 Runs
        numRuns[3] = 60;  // 60 Runs
        numRuns[4] = 80;  // 80 Runs
        numRuns[5] = 100; // 100 Runs
        
        // Confidence Interval Array to store the Confidence Interval of each numRuns[i]
        ConfidenceInterval[] perc900SecRegularCallsOutputArray = new ConfidenceInterval[numRuns.length];
        ConfidenceInterval[] perc180SecSilverCallsOutputArray = new ConfidenceInterval[numRuns.length];
        ConfidenceInterval[] perc90SecGoldCallsOutputArray = new ConfidenceInterval[numRuns.length];
        ConfidenceInterval[] percBusyCrdHCallsOutputArray = new ConfidenceInterval[numRuns.length];
        ConfidenceInterval[] percBusyRegularCallsOutputArray = new ConfidenceInterval[numRuns.length];
        
        
        // Double Array to store the output values after each individual run
        double[] perc900SecRegularCallsArray;
        double[] perc180SecSilverCallsArray;
        double[] perc90SecGoldCallsArray;
        double[] percBusyCrdHCallsArray;
        double[] percBusyRegularCallsArray;
        
        
        // Nested For Loop which increases the total numRuns with j and simulates the current run with k
        // Case 1
        //Schedule:    Trunk Lines: 250   Reserved Lines: 100
        for (int j = 0; j<numRuns.length; j++)
        {
         // Lets get a set of uncorrelated seeds
         sds = new Seeds[numRuns[j]];

            for(int i=0 ; i<numRuns[j] ; i++) 
             sds[i] = new Seeds(rsg);
            
            
            // Double Array to store the output values after each individual run
            perc900SecRegularCallsArray = new double[numRuns[j]];
            perc180SecSilverCallsArray = new double[numRuns[j]];
            perc90SecGoldCallsArray = new double[numRuns[j]];
            percBusyCrdHCallsArray = new double[numRuns[j]];
            percBusyRegularCallsArray = new double[numRuns[j]];
         
         for (int k = 0; k<numRuns[j]; k++)
         {
          //Running the Simulation Model for Case 1
          SMT1 = new SMTravel(startTime,endTime,schedules,numLines,numReserved,sds[k],false);
          SMT1.runSimulation();
          // Calculate total cost
	      int totalRegularOperators = 0;
		  int totalSilverOperators = 0;
		  int totalGoldOperators = 0;
		  
		  for(int r = 0 ; r < 5; r++ ) {
			  totalRegularOperators += schedules[0][r];
			  totalSilverOperators += schedules[1][r];
			  totalGoldOperators += schedules[2][r];
		  }
	  	  double totalCost = 8*(23* totalGoldOperators + 20* totalSilverOperators + 
	  			  			16*totalRegularOperators) + 170* (numLines - 50);
          System.out.println("Perc900SecRegularCalls:"+SMT1.getPerc900SecRegularCalls());
          System.out.println("Perc180SecSilverCalls:"+SMT1.getPerc180SecSilverCalls());
          System.out.println("Perc90SecGoldCalls:"+SMT1.getPerc90SecGoldCalls());
          System.out.println("PercBusyCrdHCalls:"+SMT1.getPercBusyCardHCalls());
          System.out.println("PercBusyRegularCalls:"+SMT1.getPercBusyRegularCalls());
          System.out.println("Total Cost is: $" + totalCost);
           perc900SecRegularCallsArray[k] = SMT1.getPerc900SecRegularCalls();
             perc180SecSilverCallsArray[k] = SMT1.getPerc180SecSilverCalls();
             perc90SecGoldCallsArray[k] = SMT1.getPerc90SecGoldCalls();
             percBusyCrdHCallsArray[k] = SMT1.getPercBusyCardHCalls();
             percBusyRegularCallsArray[k] = SMT1.getPercBusyRegularCalls();
         }
         
         //The Confidence Interval for each numRuns (i.e. 10, 20, 40,...) is placed in a Confidence Interval Array
         perc900SecRegularCallsOutputArray[j] = new ConfidenceInterval(perc900SecRegularCallsArray, 0.9);
         perc180SecSilverCallsOutputArray[j] = new ConfidenceInterval(perc180SecSilverCallsArray, 0.9);
         perc90SecGoldCallsOutputArray[j] = new ConfidenceInterval(perc90SecGoldCallsArray, 0.9);
         percBusyCrdHCallsOutputArray[j] = new ConfidenceInterval(percBusyCrdHCallsArray, 0.9);
         percBusyRegularCallsOutputArray[j] = new ConfidenceInterval(percBusyRegularCallsArray, 0.9);
        }
        
        
        
        //Confidence Interval Output for Each Output Variable where the total number of runs is varied in between
        
        
        //Formatting the Output String
        String printFormat = "| %25s | %10.6f | %10.6f | %10.6f | %10.6f | %10.6f | %10.6f |";
        
        //Outputing the Percentage of 900 Second Regular Calls as a Confidence Interval
        System.out.println("Perc900SecRegularCalls:");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        System.out.format("| Number of Runs            | PE         | S(n)       | Zeta       |   CI Min   | CI Max     | Zeta/PE    |");
        System.out.println("");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        for (int a = 0; a < numRuns.length; a++)
        {
         
         System.out.format(printFormat, numRuns[a], perc900SecRegularCallsOutputArray[a].getPointEstimate(),
           perc900SecRegularCallsOutputArray[a].getStdDev(), perc900SecRegularCallsOutputArray[a].getZeta(),
           perc900SecRegularCallsOutputArray[a].getCfMin(), perc900SecRegularCallsOutputArray[a].getCfMax(),
           perc900SecRegularCallsOutputArray[a].getZeta()/perc900SecRegularCallsOutputArray[a].getPointEstimate());
            System.out.println("");
            
        }
        
        //Outputing the Percentage of 180 Second Silver Calls as a Confidence Interval
        System.out.println("Perc180SecSilverCalls:");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        System.out.format("| Number of Runs            | PE         | S(n)       | Zeta       |   CI Min   | CI Max     | Zeta/PE    |");
        System.out.println("");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        for (int a = 0; a < numRuns.length; a++)
        {
         
         System.out.format(printFormat, numRuns[a], perc180SecSilverCallsOutputArray[a].getPointEstimate(),
           perc180SecSilverCallsOutputArray[a].getStdDev(), perc180SecSilverCallsOutputArray[a].getZeta(),
           perc180SecSilverCallsOutputArray[a].getCfMin(), perc180SecSilverCallsOutputArray[a].getCfMax(), 
           perc180SecSilverCallsOutputArray[a].getZeta()/perc180SecSilverCallsOutputArray[a].getPointEstimate());
            System.out.println("");
            
        }
        
        //Outputing the Percentage of 90 Second Gold Calls as a Confidence Interval
        System.out.println("Perc90SecGoldCalls:");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        System.out.format("| Number of Runs            | PE         | S(n)       | Zeta       |   CI Min   | CI Max     | Zeta/PE    |");
        System.out.println("");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        for (int a = 0; a < numRuns.length; a++)
        {
         
         System.out.format(printFormat, numRuns[a], perc90SecGoldCallsOutputArray[a].getPointEstimate(),
           perc90SecGoldCallsOutputArray[a].getStdDev(), perc90SecGoldCallsOutputArray[a].getZeta(),
           perc90SecGoldCallsOutputArray[a].getCfMin(), perc90SecGoldCallsOutputArray[a].getCfMax(),
           perc90SecGoldCallsOutputArray[a].getZeta()/perc90SecGoldCallsOutputArray[a].getPointEstimate());
            System.out.println("");
            
        }
       
        //Outputing the Percentage of Busy Signals for Cardholder Calls as a Confidence Interval
        System.out.println("percBusyCrdHCalls:");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        System.out.format("| Number of Runs            | PE         | S(n)       | Zeta       |   CI Min   | CI Max     | Zeta/PE    |");
        System.out.println("");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        for (int a = 0; a < numRuns.length; a++)
        { 
         System.out.format(printFormat, numRuns[a], percBusyCrdHCallsOutputArray[a].getPointEstimate(),
           percBusyCrdHCallsOutputArray[a].getStdDev(), percBusyCrdHCallsOutputArray[a].getZeta(),
           percBusyCrdHCallsOutputArray[a].getCfMin(), percBusyCrdHCallsOutputArray[a].getCfMax(),
           percBusyCrdHCallsOutputArray[a].getZeta()/percBusyCrdHCallsOutputArray[a].getPointEstimate());
            System.out.println("");      
        }
        
        //Outputing the Percentage of Busy Signals for Regular Calls as a Confidence Interval
        System.out.println("percBusyRegularCalls:");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        System.out.format("| Number of Runs            | PE         | S(n)       | Zeta       |   CI Min   | CI Max     | Zeta/PE    |");
        System.out.println("");
        
        System.out.format("|---------------------------|------------|------------|------------|------------|------------|------------|");
        System.out.println("");
        
        for (int a = 0; a < numRuns.length; a++)
        {
         System.out.format(printFormat, numRuns[a], percBusyRegularCallsOutputArray[a].getPointEstimate(),
           percBusyRegularCallsOutputArray[a].getStdDev(), percBusyRegularCallsOutputArray[a].getZeta(),
           percBusyRegularCallsOutputArray[a].getCfMin(), percBusyRegularCallsOutputArray[a].getCfMax(),
           percBusyRegularCallsOutputArray[a].getZeta()/percBusyRegularCallsOutputArray[a].getPointEstimate());
            System.out.println("");      
        }
        
    }
    
}

