package github.aq.cryptoprofittracker.parse.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import github.aq.cryptoprofittracker.model.Transaction;

public class BinanceDepositTransactionsCsvReader {

	
	public static List<Transaction> read(String filename) {
		List<Transaction> list = new ArrayList<>();
		
		Iterable<CSVRecord> records = FileReader.read(filename);

		for (CSVRecord record : records) {
			Transaction tran = new Transaction();
			
			DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //MMM. DD, YYYY, HH:mm PM/AM
			String dateTimeValue = record.get("Date");
			LocalDateTime ldt = LocalDateTime.parse(dateTimeValue, dTF);
			tran.setDateTime(ldt);
			
			tran.setMarketType("DEPOSIT");
			String currency = record.get("Coin");
			String amount = record.get("Amount");
			tran.setAmount(amount, currency);
			
			String fee = record.get("TransactionFee");
			tran.setFee(fee, currency);
			
			// record.get("Address");
			tran.setWebsiteTxId(record.get("TXID"));
			list.add(tran);
		}
		return list;
	}
}
