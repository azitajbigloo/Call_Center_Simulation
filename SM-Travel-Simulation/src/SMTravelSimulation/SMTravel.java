package SMTravelSimulation;
import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;
import simulationModelling.ExtSequelActivity;
import simulationModelling.SBNotice;
import simulationModelling.SequelActivity;
import SMTravelSimulation.CardHolderCallArrival;
import SMTravelSimulation.EstimateWaitTime;
import SMTravelSimulation.Initialise;
import SMTravelSimulation.Output;
import SMTravelSimulation.RVPs;
import SMTravelSimulation.RegularCallArrival;
import SMTravelSimulation.Seeds;
import SMTravelSimulation.Service;
import SMTravelSimulation.StaffChange;
import SMTravelSimulation.UDPs;
/*
 * The Simulation Model Class
 */ 
public class SMTravel extends AOSimulationModel{
	
	protected boolean traceFlag;

/*----------------------Entity Data Structures--------------------------*/
	/* Group and Queue Entities */
		Operator[] rgOperator = new Operator[3];
		CallLine[] qCallLines = new CallLine[3];	
		TrunkLines rgTrunkLine = new TrunkLines(); 
		
	/* Parameters*/
		// RG.TrunkLines.numLines: Implemented as an attribute of rgTrunkline 
		// RG.TrunkLines.numReserved: Implemented as an attribute of rgTrunkline 
		// RG.Operators[REGULAR].shift: Implemented as an attribute of rgOperator 
		// RG.Operators[SILVER].shift: Implemented as an attribute of rgOperator 
		// RG.Operators[GOLD].shift: Implemented as an attribute of rgOperator 
		
	/* Input Variables */
		// Define any Independent Input Variables here
		
		// References to RVP and UDP objects
		 RVPs rvp;  // Reference to rvp object - object created in constructor
		 UDPs udp = new UDPs();

		// Output object
		 Output output = new Output();
		
		// Output values - define the public methods that return values
		// required for experimentation.
		//The percentage of regular calls that exceeded the 900 second wait time.
		public double getPerc900SecRegularCalls(){
			 return output.getPerc900SecRegularCalls();
		}
		//The  percentage of silver calls that exceeded the 180 second wait time.
		public double getPerc180SecSilverCalls(){
			 return output.getPerc180SecSilverCalls();
		}
		//The percentage of gold calls that exceeded the 90 second wait time.
		public double getPerc90SecGoldCalls(){
			return output.getPerc90SecGoldCalls();
		}
		//The percentage of card hold calls that receive busy signal.
		public double getPercBusyCardHCalls(){
			return output.getPercBusyCardHCalls();
		}
		//The percentage of regular calls that receive a busy signal.
		public double getPercBusyRegularCalls(){
			return output.getPercBusyRegularCalls();
		}
		
		public int getNumRegularCalls(){
			return output.ssov_numRegularCalls;
		}
		public int getNumSilverCalls(){
			return output.ssov_numSilverCalls;
		}
		public int getNumGoldCalls(){
		    return output.ssov_numGoldCalls;
		}
		public int getNumBusyRegularCalls(){
			return output.ssov_numBusyRegularCalls;
		}
		public int getNumBusyCardHCalls(){
			return output.ssov_numBusyCardHCalls;
		}
		public int getNum900SecRegularCalls(){
			return output.ssov_num900SecRegularCalls;
		}
		public int getNum180SecSilverCalls(){
			return output.ssov_num180SecSilverCalls;
		}
		public int getNum90SecGoldCalls(){
			return output.ssov_num90SecGoldCalls;
		}
		
		//constructor
		public SMTravel(double t0time, double tftime, int[][] schedule, int numTrunkLine,
           int numReservedLine, Seeds sd, boolean traceFlag) {
			initialiseClasses(sd);
			
			// Tracing
			 this.traceFlag = traceFlag;
			// Create RVP object with given seed
			 rvp = new RVPs(sd);
			
			 // Initialize Parameters
			rgTrunkLine = new TrunkLines();
			rgTrunkLine.numLines = numTrunkLine;
			rgTrunkLine.numReserved = numReservedLine;
			
			rgOperator[Constants.REGULAR] = new Operator();
			rgOperator[Constants.SILVER] = new Operator();
			rgOperator[Constants.GOLD] = new Operator();
			rgOperator[Constants.REGULAR].schedule = schedule[Constants.REGULAR];
			rgOperator[Constants.SILVER].schedule = schedule[Constants.SILVER];
			rgOperator[Constants.GOLD].schedule = schedule[Constants.GOLD];
					
			qCallLines[Constants.REGULAR] = new CallLine();
			qCallLines[Constants.SILVER] = new CallLine();
			qCallLines[Constants.GOLD] = new CallLine();			
		         
			// Initialize the Simulation Model, 
			this.initAOSimulModel(t0time,tftime);
		    closingTime = tftime;
			// Schedule the first arrivals and employee scheduling
			Initialise init = new Initialise();
			scheduleAction(init);  // Should always be first one scheduled.
			StaffChange staffChange = new StaffChange();
			scheduleAction(staffChange);
			RegularCallArrival regularArrival = new RegularCallArrival();
			scheduleAction(regularArrival);
			CardHolderCallArrival cardholderArrival = new CardHolderCallArrival();
			scheduleAction(cardholderArrival);			

		}
		//Initialize classes
		void initialiseClasses(Seeds sd)
		{
			// Add reference to standard classes
			Initialise.model = this;
			Output.model = this;
			RVPs.model = this;
			UDPs.model = this;
			
			// Add reference to activity/action classes
			EstimateWaitTime.model = this;
			StaffChange.model = this;
			RegularCallArrival.model = this;
			CardHolderCallArrival.model = this;
			Service.model = this;
			InputMemberNumber.model = this;
			AfterCall.model = this;
			
			// Initialize RVPs in the classes
			RegularCallArrival.initRvps(sd);
			CardHolderCallArrival.initRvps(sd);
			EstimateWaitTime.initRvps(sd);
			InputMemberNumber.initRvps(sd);
			Service.initRvps(sd);
			AfterCall.initRvps(sd);			
		}		
		
		// Testing preconditions			
		@Override
		public void testPreconditions(Behaviour behObj)
		{
			reschedule(behObj);
			while (scanPreconditions() == true) /* repeat */;
		}
		
		// Scan preconditions
		private boolean scanPreconditions()
		{
			boolean statusChanged = false;
			
			if (Service.precondition(this) == true) {
				Service act = new Service();
				act.startingEvent();
				scheduleActivity(act);
				statusChanged = true;
			}
			
			if(statusChanged) {
				scanInterruptPreconditions();
			}
			else {
				statusChanged = scanInterruptPreconditions();
			}
			
			return (statusChanged);
		}
		
		// Scan interruptions in extended activities
		/*
		 * This is a general method that will process all extended activities.
		 * Note that the activities are terminated by default.
		 */
		private boolean scanInterruptPreconditions(){
			int num; // number of entries in list
			int interruptionNum;  // interruption number
			SBNotice nt;
			Behaviour obj;
			boolean statusChanged = false;

			num = esbl.size();
			for(int i = 0; i < num ; i++){
				nt = esbl.get(i);
				obj = (esbl.get(i)).behaviourInstance;
				if(ExtSequelActivity.class.isInstance(obj))
				{
					ExtSequelActivity extCondObj = (ExtSequelActivity) nt.behaviourInstance;
					interruptionNum = extCondObj.interruptionPreCond();
					if(interruptionNum != 0)
					{
						extCondObj.interruptionSCS(interruptionNum);
						statusChanged = true;
						unscheduleBehaviour(nt);
						i--; num--; // removed an entry, num decremented by 1 and need to look at same index for next loop
					}			
				}						
			}
			return(statusChanged);
		}
		
		public void eventOccured()
		{ 
			
			if(traceFlag) {
	           System.out.printf("Clock: %-9.3f RG.TrunkLines.n: %d\n",
	                    getClock(), rgTrunkLine.n);
	          
	           System.out.printf("Q.CallLine[REGULAR].n: %d Q.CallLine[SILVER].n: %d Q.CallLine[GOLD].n %d\n",
	                    qCallLines[Constants.REGULAR].getN(),
	                    qCallLines[Constants.SILVER].getN(),
	                    qCallLines[Constants.GOLD].getN());
	          
	           System.out.printf("RG.Operator[REGULAR].uNumOperators: %d\n",
	                   rgOperator[Constants.REGULAR].uNumOperators);
	           System.out.printf("RG.Operator[SILVER].uNumOperators:  %d\n",
	                   rgOperator[Constants.SILVER].uNumOperators);
	           System.out.printf("RG.Operator[GOLD].uNumOperators:    %d\n",
	                    rgOperator[Constants.GOLD].uNumOperators);
	       /*
	           System.out.println("numRegCalls:" + getNumRegularCalls());     
	           System.out.println("numSliverCalls:" + getNumSilverCalls());      
	           System.out.println("numGoldCalls:" + getNumGoldCalls());      
	           System.out.println("numBusyRegularCalls:"+ getNumBusyRegularCalls());   
	           System.out.println("numBusyCardholderCalls: " + getNumBusyCardHCalls());    
	           System.out.println("num900secRegularCalls: " + getNum900SecRegularCalls());  
	           System.out.println("num180secSilverCalls: " + getNum180SecSilverCalls());     
	           System.out.println("num90secGoldCall: " + getNum90SecGoldCalls());
	           System.out.println("numCardHArrivalCalls-ssov: " + output.ssov_numCardHArrivalCalls); 
	           System.out.println("numRegularArrivalCalls-ssov: " + output.ssov_numRegularArrivalCalls);
	           System.out.println("numHangUp: " + output.numHangUp);    
	           System.out.println("%percBusyRegularCalls: " + getPercBusyRegularCalls());        
	           System.out.println("%percBusyCardHCalls: " + getPercBusyCardHCalls());
	           System.out.println("%90secGold: " + getPerc90SecGoldCalls());
	           System.out.println("%180secSilver: " + getPerc180SecSilverCalls());
	           System.out.println("%900secRegular: " + getPerc900SecRegularCalls());
	        */
	           this.showSBL();
	           this.showESBL();
			}
		}		
			
/***************** Implementation of Data Modules ************/
	
	protected double closingTime; // closing time of the call center
	
	//Termination explicit
	@Override
	public boolean implicitStopCondition() {
		boolean retVal = false;
		//System.out.println("ClosingTime = " + closingTime + "currentTime = "
		//		+ getClock() + "RG.TrunkLine.n = " + rgTrunkLine.n);
		if (getClock() >= closingTime) //&& rgTrunkLine.n == 0)
			retVal = true;
		//System.out.println("implicit stop condition returns " + retVal);

		return (retVal);
	}

	// Standard Procedure to start Sequel Activities
	public void spStart(SequelActivity seqAct){
		seqAct.startingEvent();
		scheduleActivity(seqAct);
		
	}

	// Terminate
	public void spTerminate() {
	}

}
