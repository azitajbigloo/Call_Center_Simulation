import SMTravelSimulation.SMTravel;
import SMTravelSimulation.Seeds;
import cern.jet.random.engine.*;
// additional experiment
public class ExperimentCC { 
	  // print method
	  public static void printShift(int[][] currentSchedules) {
	     for (int h = 0; h < currentSchedules.length; h++) {
	        if (h==0)
	          System.out.println("Regular Operators: ");
	        if (h==1)
	          System.out.println("Silver Operators: ");
	        if (h==2)
	          System.out.println("Gold Operators: ");
	        
	        for (int g = 0; g< currentSchedules[h].length; g++)
	        {
	          if (g==0)
	            System.out.print("7AM: " + currentSchedules[h][g] + " ");
	          if (g==1)
	            System.out.print("8AM: " + currentSchedules[h][g] + " ");
	          if (g==2)
	            System.out.print("9AM: " + currentSchedules[h][g] + " ");
	          if (g==3)
	            System.out.print("10AM: " + currentSchedules[h][g] + " ");
	          if (g==4)
	            System.out.print("11AM: " + currentSchedules[h][g] + " ");
	        }
	        System.out.println("");
	      }
	  }
	  // copy array method
	  public static int[][] copyArray(int[][] toCopy) {
		  
		  int[][] copiedArray = new int[toCopy.length][toCopy[0].length];
	    
	    for(int i=0; i< toCopy.length; i++)
	    {
	      for(int j=0; j< toCopy[i].length; j++)
	    {
	      copiedArray[i][j]=toCopy[i][j];
	    }
	    }
	    return copiedArray;
	  }
	  // get the index of shift to add operator to
	  public static int shiftIdentification(int[][] currentSchedules, double startTime, double endTime, int numLines, int numReserved, String type)
	    {
	     int [] [] newSchedules = copyArray(currentSchedules);
	      
	      SMTravel RegularTest = null;
	      int indexWaitTime = 0;
	      int numShifts = 5;
	      SMTravel[] Shift = new SMTravel[numShifts];
	      RandomSeedGenerator rsg = new RandomSeedGenerator(); 
	      
	      if (type.equals("Gold"))
	      {
	        for (int i=0; i<numShifts; i++)
	        {
	          newSchedules[0][i] = newSchedules[0][i] + 1;
	          Shift[i] = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	          Shift[i].runSimulation();
	          newSchedules = copyArray(currentSchedules);
	      
	          if (i==0)
	            RegularTest = Shift[i];
	          
	          else if(Shift[i].getPerc90SecGoldCalls() < RegularTest.getPerc90SecGoldCalls())
	          {
	            RegularTest=Shift[i];
	            indexWaitTime = i;
	          }
	          System.out.println("Perc90SecGoldCalls:"+Shift[i].getPerc90SecGoldCalls());
	        }
	      }
	       else if (type.equals("Silver"))
	      {
	        for (int i=0; i<numShifts; i++)
	        {
	          newSchedules[0][i] = newSchedules[0][i] + 1;
	          Shift[i] = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	          Shift[i].runSimulation();
	          newSchedules = copyArray(currentSchedules);
	      
	          if (i==0)
	            RegularTest = Shift[i];
	          
	          else if(Shift[i].getPerc180SecSilverCalls() < RegularTest.getPerc180SecSilverCalls())
	          {
	            RegularTest=Shift[i];
	            indexWaitTime = i;
	          }
	          System.out.println("Perc180SecSilverCalls:"+Shift[i].getPerc180SecSilverCalls());
	        }
	      }
	      else if (type.equals("Regular"))
	      {
	        for (int i=0; i<numShifts; i++)
	        {
	          newSchedules[0][i] = newSchedules[0][i] + 1;
	         
	          Shift[i] = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	          Shift[i].runSimulation();
	          newSchedules = copyArray(currentSchedules);
	      
	          if (i==0)
	            RegularTest = Shift[i];
	          
	          else if(Shift[i].getPerc900SecRegularCalls() < RegularTest.getPerc900SecRegularCalls())
	          {
	            RegularTest=Shift[i];
	            indexWaitTime = i;
	          }
	          System.out.println("Perc900SecRegularCalls:"+Shift[i].getPerc900SecRegularCalls());
	        }
	      }
	     
	     return indexWaitTime;
	    }
	  // main method   
	  	public static void main(String[] args)
	    {

	     int numLines = 250;
	     int numReserved = 100;
	     double startTime=0.0;
	     double endTime=720.0;
	     
	     int [] [] schedules =
	      {
	    	  { 12, 12, 12, 12, 12},  // Regular 
	 	     { 0, 0, 0, 0,0},  // Silver
	 	     {0, 0, 0, 0, 0}  // Gold    
	      };
	      SMTravel Base;
	      RandomSeedGenerator rsg = new RandomSeedGenerator(); 
	      Base = new SMTravel(startTime,endTime,schedules,numLines,numReserved,new Seeds(rsg),false);
	      Base.runSimulation();
	      
	      SMTravel CurrentTest = Base;
	      int [][] currentSchedules = copyArray(schedules);
	        
	      
	
	      while(CurrentTest.getPerc180SecSilverCalls() > 0.05 || CurrentTest.getPerc900SecRegularCalls() > 0.15 || CurrentTest.getPerc90SecGoldCalls()> 0.02)
	      {
	   
	      //new local variable newSchedules
	        int [] [] newSchedules = copyArray(currentSchedules);
	      
	      //get index for which shift an operator is being added to
	       int index =  shiftIdentification(newSchedules, startTime, endTime, numLines, numReserved, "Gold");

	       //increase number of regular operators at that shift
	       newSchedules[0][index] += 1;
	       SMTravel RegularTest = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	       RegularTest.runSimulation();
	      
	       SMTravel SilverTest, GoldTest;
	     
	       //check to see if addition of regular operator was enough 
	       
	      if (RegularTest.getPerc90SecGoldCalls() > 0.02)
	         {
	       //Reset newSchedules to check impact for the addition of Silver Operators
	         newSchedules = copyArray(currentSchedules);
	         
	       //increase number of silver operators at that shift
	          newSchedules[1][index] = newSchedules[1][index] + 1; 
	          SilverTest = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	          SilverTest.runSimulation();
	          
	          if (SilverTest.getPerc90SecGoldCalls() > 0.02)
	          {
	         //Reset newSchedules to check impact for the addition of Gold Operators
	           newSchedules = copyArray(currentSchedules);
	           
	           //increase number of gold operators at that shift
	           newSchedules[2][index] = newSchedules[2][index] + 1;  
	           GoldTest = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	           GoldTest.runSimulation();
	          
	         //Set global variable (Addition of Gold Operator is going to give better output percentages for gold calls than the addition of Silver/Regular Operator)
	           CurrentTest = GoldTest;
	           currentSchedules = copyArray(newSchedules);
	          }
	          else
	          {
	            //Set global variable (Addition of Silver Operator is going to give better output percentages for gold calls than the addition of Regular Operator)
	            CurrentTest = SilverTest;
	            currentSchedules = copyArray(newSchedules);
	          }
	         }
	      else
	      {
	        //Set global variable (Addition of Regular Operator is going to give better output percentages for gold calls than the addition of No Operator)
	        CurrentTest = RegularTest;
	        currentSchedules = copyArray(newSchedules);
	      }
	      
	      if (CurrentTest.getPerc180SecSilverCalls() > 0.05)
	       {
	         //get index for which shift an operator is being added to
	          index =  shiftIdentification(newSchedules, startTime, endTime, numLines, numReserved, "Silver");	         
	       
	         //increase number of regular operators at that shift
	        newSchedules[0][index] += 1;
	        RegularTest = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	        RegularTest.runSimulation();
	      
	        
	        if (RegularTest.getPerc180SecSilverCalls() > 0.05)
	         {
	        //Reset newSchedules to check impact for the addition of Silver Operators
	         newSchedules = copyArray(currentSchedules);
	         
	         //increase number of silver operators at that shift
	         newSchedules[1][index] = newSchedules[1][index] + 1;  
	         SilverTest = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	         SilverTest.runSimulation();
	         
	         //Set global variable (Addition of Silver Operator is going to give better output percentages for silver calls than the addition of Regular Operator)
	          CurrentTest = SilverTest;
	          currentSchedules = copyArray(newSchedules);
	         }
	        else
	        {
	          //Set global variable (Addition of Regular Operator is going to give better output percentages for silver calls than the addition of No Operator)
	          CurrentTest = RegularTest;
	          currentSchedules = copyArray(newSchedules);
	        }
	       }
     
	      
	       if (CurrentTest.getPerc900SecRegularCalls() > 0.15)
	       {
	        //get index for which shift an operator is being added to
	         index =  shiftIdentification(currentSchedules, startTime, endTime, numLines, numReserved,"Regular" );
	       
	         //increase number of regular operators at that shift
	       newSchedules[0][index] += 1;
	        
	       
	       RegularTest = new SMTravel(startTime,endTime,newSchedules,numLines,numReserved,new Seeds(rsg),false);
	       RegularTest.runSimulation();
	       
	       CurrentTest = RegularTest;
	       currentSchedules = copyArray(newSchedules);
	       }

	       //System.out.println("**Schedule After Third If:");
	       //printShift( currentSchedules);
	      }
	      
	      //Printing Solution to Console
	      System.out.println("------Solution Obtained------");
	      
	      System.out.println("Perc900SecRegularCalls:"+CurrentTest.getPerc900SecRegularCalls());
	      System.out.println("Perc180SecSilverCalls:"+CurrentTest.getPerc180SecSilverCalls());
	      System.out.println("Perc90SecGoldCalls:"+CurrentTest.getPerc90SecGoldCalls());
	      System.out.println("PercBusyCrdHCalls:"+CurrentTest.getPercBusyCardHCalls());
	      System.out.println("PercBusyRegularCalls:"+CurrentTest.getPercBusyRegularCalls());
	      
	     for (int h = 0; h < currentSchedules.length; h++)
	     {
	       if (h==0)
	         System.out.println("Regular Operators: ");
	       if (h==1)
	         System.out.println("Silver Operators: ");
	       if (h==2)
	         System.out.println("Gold Operators: ");
	       
	       for (int g = 0; g< currentSchedules[h].length; g++)
	       {
	         if (g==0)
	           System.out.print("7AM: " + currentSchedules[h][g] + " ");
	         if (g==1)
	           System.out.print("8AM: " + currentSchedules[h][g] + " ");
	         if (g==2)
	           System.out.print("9AM: " + currentSchedules[h][g] + " ");
	         if (g==3)
	           System.out.print("10AM: " + currentSchedules[h][g] + " ");
	         if (g==4)
	           System.out.print("11AM: " + currentSchedules[h][g] + " ");
	       }
	       System.out.println("");
	     }
	     
	    numLines = 50;
	    numReserved=0;
	   
	    
	    while (!(CurrentTest.getPercBusyRegularCalls() < 0.02) || !(CurrentTest.getPercBusyCardHCalls() < 0.2) )
	    {
	      CurrentTest = new SMTravel(startTime,endTime,currentSchedules,numLines,numReserved,new Seeds(rsg),false);
	      CurrentTest.runSimulation();
	      if (CurrentTest.getPercBusyRegularCalls() > 0.2)
	      {
	        numLines += 5;
	      }
	      if (CurrentTest.getPercBusyCardHCalls() > 0.02)
	      {
	        numReserved += 1; 
	      }   
	    }
	    
	    System.out.println("Number of Trunk Lines: " + numLines);
	    System.out.println("Number of Reserved Lines: " + numReserved);
	    
	     int totalRegularOperators = 0;
			  int totalSilverOperators = 0;
			  int totalGoldOperators = 0;
		  for(int r = 0 ; r < 5; r++ ) {
			   totalRegularOperators += currentSchedules[0][r];
			   totalSilverOperators += currentSchedules[1][r];
			   totalGoldOperators += currentSchedules[2][r];
		  }
		  double totalCost = 8*(23* totalGoldOperators + 20* totalSilverOperators +  16*totalRegularOperators) + 170* (numLines - 50);
		    System.out.println("Total Cost is: $" + totalCost);
	  }
	  
	 }

