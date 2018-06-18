package github.aq.cryptoinvestmentwatcher.model;

public class TransactionIdentifier {

	public static long idCounter = 0;
	
	public static long computeNextId() {
		idCounter ++;
		return idCounter;
	}
}
