package github.aq.cryptoprofittracker.model;

import java.util.ArrayList;
import java.util.List;

public class Transactions {

	private static Transactions instance; 
	List<Transaction> transactions = new ArrayList<Transaction>();
	
	private Transactions() {
		
	}
	
	public static Transactions getInstance() {
		if (instance == null) {
			instance = new Transactions();
		}
		return instance;
	}
	
	public void addTransaction(Transaction sale) {
		transactions.add(sale);
	}

	public int size() {
		return transactions.size();
	}

	public List<Transaction> getTransactionList() {
		return transactions;
	}
}
