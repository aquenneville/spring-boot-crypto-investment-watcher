package github.aq.cryptoprofittracker.model;

public class TransactionIdentifier {

	public static long idCounter = 0;
	
	public static long computeNextId() {
		idCounter ++;
		return idCounter;
	}
}
