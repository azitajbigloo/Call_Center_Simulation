package SMTravelSimulation;
import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ConditionalActivity;
/*
 * Conditional Activity Service 
 */
public class Service extends ConditionalActivity{

	static SMTravel model;
	private Call icCall;
	private int operatorId; 
	private int callLineId;

   protected static boolean precondition(SMTravel model){
	   boolean returnValue = false;
	   if(udpGetOperatorIdToService() != Constants.NONE && udpGetCallLineId() != Constants.NONE) {
		   returnValue = true ;
	   }
	   return returnValue;
   }
	   
   public void startingEvent(){
	   callLineId = udpGetCallLineId(); 
	   operatorId = udpGetOperatorIdToService();
	   icCall = (Call) model.qCallLines[callLineId].spRemoveQue();
	   model.rgOperator[operatorId].numBusy++;
	   model.udp.UpdateWaitCallsOutput(icCall);
   }
   
   protected double duration(){
	   return (rvpuSrvTm(callLineId,operatorId));
   }
   
   protected void terminatingEvent(){  
	   model.rgTrunkLine.n--;
	   model.udp.UpdateNumArrivalsOutput(icCall);
	   AfterCall afterCall = new AfterCall(icCall.uCaType, operatorId);
	   model.spStart(afterCall);
   }

/*------------------------------------   Embedded UDPs   ----------------------------------------------*/
   /* Method: udpGetOperatorIdToService
    * Description: get the proper available operators Id to start the service
    * Parameter: nothing
    * Return: the operatorId
    */
   protected static int udpGetOperatorIdToService() {
	   int operatorId = Constants.NONE;
	   for (int opType = Constants.GOLD; opType >= Constants.REGULAR && operatorId == Constants.NONE; opType--) { //Prioritize GOLD operators
		   if(model.rgOperator[opType].numBusy < model.rgOperator[opType].uNumOperators) {
			   if (!model.qCallLines[Constants.GOLD].spIsEmpty()){
				   operatorId = opType;         
			   }
			   else if ((opType == Constants.REGULAR || opType == Constants.SILVER)
					   && !model.qCallLines[Constants.SILVER].spIsEmpty()) {
				   operatorId = opType;
			   }
			   else if (opType == Constants.REGULAR
					   && !model.qCallLines[Constants.REGULAR].spIsEmpty()) {
				   operatorId = opType;
			   }
		   }
	   }
	   return operatorId;
   }
   /* Method: udpGetCallLineId
    * Description: get the callLine Id with the call to start service
    * Parameter: nothing
    * Return: the callLineId
    */
   protected static int udpGetCallLineId() {
	   int CallLineId = Constants.NONE;
		 for (int caType = Constants.GOLD; caType >= Constants.REGULAR && CallLineId ==Constants.NONE; caType--) { //Prioritize GOLD operators
			 if(model.rgOperator[caType].numBusy < model.rgOperator[caType].uNumOperators) {
				 if (!model.qCallLines[Constants.GOLD].spIsEmpty()){
					 CallLineId = Constants.GOLD;
				 } 
				 else if ((caType == Constants.REGULAR || caType == Constants.SILVER)
						 && !model.qCallLines[Constants.SILVER].spIsEmpty()) {
					 CallLineId = Constants.SILVER;
				 }
				 else if (caType == Constants.REGULAR
						 && !model.qCallLines[Constants.REGULAR].spIsEmpty()) {
					 CallLineId = Constants.REGULAR;
				 }
			 }
		 }
		 return CallLineId;
   	}

 /*---------------------------------------- Embedded RVPs --------------------------------------------------*/	
   static void initRvps(Seeds sd) {
	   // Initialise Internal modules, user modules and input variables
		 dmSrvTm = new TriangularVariate[3];
		 for (int i = 0; i < 3; i++){
			 dmSrvTm[i] = new TriangularVariate(
					 SERVICE_TIME[i][MIN],
					 SERVICE_TIME[i][MEAN],
	                 SERVICE_TIME[i][MAX],
	                 new MersenneTwister(sd.serviceTime[i]));
		 }				
   }	
   private static TriangularVariate[] dmSrvTm;
   protected final static int MIN = 0;
   protected final static int MAX = 1;
   protected final static int MEAN = 2;
   protected final static double SILVER_OPERATOR_REDUCTION = 0.95; //95% of the time needed by a REGULAR operator
   protected final static double GOLD_OPERATOR_REDUCTION = 0.88;   //88% of the time needed by a REGULAR operator
   protected final static double[][] SERVICE_TIME = {
	            {1.2,  3.75, 2.05}, //INFORMATION
	            {2.25, 8.6,  2.95}, //RESERVATION
	            {1.2,  5.8,  1.9 }  //CHANGE
   };
   // RVP for operators service time
   static protected double rvpuSrvTm(int caType, int operatorType){       
	   double serviceTime = dmSrvTm[caType].next();
	   if (operatorType == Constants.SILVER) 
		   serviceTime *= SILVER_OPERATOR_REDUCTION;        
	   else if (operatorType == Constants.GOLD) 
		   serviceTime *= GOLD_OPERATOR_REDUCTION;
	   return serviceTime;
   }
}