import SMTravelSimulation.SMTravel;
import SMTravelSimulation.Seeds;
import cern.jet.random.engine.*;
// Experiments, for trace logging 
public class Experiment1 {

 
   static int [] [] schedules =
   {
		   { 3, 3, 3, 3, 3},  // Regular 
		   { 0, 0, 0, 0, 0},  // Silver
		   { 0, 0, 0, 0, 0}  // Gold	   
   };
// Main Method:
   public static void main(String[] args)
   {
       int i, NUMRUNS = 20; 
       double startTime=0.0, endTime=720.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMTravel SMT1;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) 
    	   sds[i] = new Seeds(rsg);
       
       // Loop for NUMRUN simulation runs for each case
       // Case 1
       System.out.println(" Case 1");
       for(i=0 ; i < NUMRUNS ; i++)
       {
    	   System.out.println("-------------------------------------"+i+"-----------------"+NUMRUNS);
          SMT1 = new SMTravel(startTime,endTime,schedules,150,5,sds[i],true);
          SMT1.runSimulation();
         // System.out.println("Perc900SecRegularCalls:"+SMT1.getPerc900SecRegularCalls());
         // System.out.println("Perc180SecSilverCalls:"+SMT1.getPerc180SecSilverCalls());
         // System.out.println("Perc90SecGoldCalls:"+SMT1.getPerc90SecGoldCalls());
         // System.out.println("PercBusyCrdHCalls:"+SMT1.getPercBusyCardHCalls());
         // System.out.println("PercBusyRegularCalls:"+SMT1.getPercBusyRegularCalls());
         // System.out.println("numRegCalls:" + SMT1.getNumRegularCalls());
         // System.out.println("numGoldCalls:" + SMT1.getNumGoldCalls());
         // System.out.println("numSliverCalls:" + SMT1.getNumSilverCalls());
         // System.out.println("numbusyregularcalls:"+SMT1.getNumBusyRegularCalls());
         // System.out.println("numbusycardholdercalls: " + SMT1.getNumBusyCardHCalls());
         // System.out.println("num900secgoldCalls: " + SMT1.getNum90SecGoldCalls());
         // System.out.println("num180secsilverCalls: " + SMT1.getNum180SecSilverCalls());
         // System.out.println("num90secregularCall: " + SMT1.getNum900SecRegularCalls());
     
       }
   }
}

