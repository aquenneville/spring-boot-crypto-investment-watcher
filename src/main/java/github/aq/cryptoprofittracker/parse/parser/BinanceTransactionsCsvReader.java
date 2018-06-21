package github.aq.cryptoprofittracker.parse.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import github.aq.cryptoprofittracker.model.Transaction;

public class BinanceTransactionsCsvReader {

	
	public static List<Transaction> read(String filename) {
		List<Transaction> list = new ArrayList<>();
		
		Iterable<CSVRecord> records = FileCsvReader.read(filename);

		for (CSVRecord record : records) {
			Transaction tran = new Transaction();
			
			list.add(tran);
		}
		return list;
	}
}
