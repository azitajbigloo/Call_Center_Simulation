package SMTravelSimulation;
import cern.jet.random.engine.RandomSeedGenerator;
/*
 * Seeds
 */
public class Seeds {   
	int arrRegular;
    int arrCardholder;
    int cardholderType;
    int[] serviceTime;
    int[] afterCallTime;
    int[] toleratedWaitTime;
    int typingTime;
    int callType;

    public Seeds(RandomSeedGenerator rsg) {   
    	arrRegular = rsg.nextSeed();
        arrCardholder = rsg.nextSeed();
        cardholderType = rsg.nextSeed();
        serviceTime = new int[]{rsg.nextSeed(), rsg.nextSeed(), rsg.nextSeed()};
        afterCallTime = new int[]{rsg.nextSeed(), rsg.nextSeed(), rsg.nextSeed()};
        toleratedWaitTime = new int[]{rsg.nextSeed(), rsg.nextSeed()};
        typingTime = rsg.nextSeed();
        callType = rsg.nextSeed();
    }
}
