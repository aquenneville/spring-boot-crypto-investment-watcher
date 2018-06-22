package github.aq.cryptoprofittracker.parse.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import github.aq.cryptoprofittracker.model.Transaction;
import github.aq.cryptoprofittracker.model.Exchange;

public class BitstampTransactionsCsvReader {
	
	public static List<Transaction> read(String filename) {
		
		List<Transaction> list = new ArrayList<>();
		Iterable<CSVRecord> records = FileReader.read(filename);
		
		for (CSVRecord record : records) {
			Transaction tran = new Transaction();
			
			//https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
			DateTimeFormatter dTF = DateTimeFormatter.ofPattern("MMM. dd, yyyy, hh:mm a"); //MMM. DD, YYYY, HH:mm PM/AM
			String dateTimeValue = record.get("Datetime");
			LocalDateTime ldt = LocalDateTime.parse(dateTimeValue, dTF);
			
		    String marketType = record.get("Type").toUpperCase();
		    String accountId = record.get("Account");
		    String amount = record.get("Amount");
		    String value = record.get("Value");
		    String rate = record.get("Rate");
		    String fee = record.get("Fee");
		    String orderType = record.get("Sub Type");
		    
		    tran.setMarketType(marketType.toUpperCase());
		    tran.setAccountId(accountId);
		    tran.setOrderType(orderType.toUpperCase());
		    if (amount.length() > 0) {
		        tran.setAmount(amount.split(" ")[0], amount.split(" ")[1]);
		    }
		    if (value.length() > 0) {
    		    tran.setValue(value.split(" ")[0], value.split(" ")[1]);
		    }
		    if (rate.length() > 0) {
    		    tran.setRate(rate.split(" ")[0], rate.split(" ")[1]);
		    }
		    if (fee.length() > 0) {
		    	tran.setFee(fee.split(" ")[0], fee.split(" ")[1]);
		    }
		    tran.setDateTime(ldt);
		    tran.setExchange(Exchange.BITSTAMP);		    
		    list.add(tran);
		}
		return list;
	}
}
