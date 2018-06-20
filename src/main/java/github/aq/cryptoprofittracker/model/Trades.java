package github.aq.cryptoprofittracker.model;

import java.util.ArrayList;
import java.util.List;

public class Trades {

	private static Trades instance; 
	List<Trade> transactions = new ArrayList<Trade>();
	
	private Trades() {}
	
	public static Trades getInstance() {
		if (instance == null) {
			instance = new Trades();
		}
		return instance;
	}
	
	public void addTransaction(Trade sale) { transactions.add(sale); }

	public int size() { return transactions.size(); }

	public List<Trade> getTransactionList() { return transactions;}
}
