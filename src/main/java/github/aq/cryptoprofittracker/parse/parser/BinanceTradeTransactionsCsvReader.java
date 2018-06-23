package github.aq.cryptoprofittracker.parse.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import github.aq.cryptoprofittracker.model.Transaction;

public class BinanceTradeTransactionsCsvReader {

	
	public static List<Transaction> read(String filename) {
		List<Transaction> list = new ArrayList<>();
		
		Iterable<CSVRecord> records = FileReader.read(filename);

		for (CSVRecord record : records) {
			Transaction tran = new Transaction();
			
			DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-mm-DD HH:mm:ss"); //MMM. DD, YYYY, HH:mm PM/AM
			String dateTimeValue = record.get("Date(UTC)");
			LocalDateTime ldt = LocalDateTime.parse(dateTimeValue, dTF);
			tran.setDateTime(ldt);
			
			String pair = record.get("Market");
			String newAsset = pair.substring(0, 2);
			String assetOrigine = pair.substring(3);
			
			String orderType = record.get("Type");
			if ("BUY".equals(orderType)) {
				tran.setOrderType(Transaction.OrderType.BUY.name());
			} else if ("SELL".equals(orderType)) {
				tran.setOrderType(Transaction.OrderType.SELL.name());
			}
			
			String rate = record.get("Price");
			tran.setRate(rate, assetOrigine);
			
			String amount = record.get("Amount");
			tran.setAmount(amount, newAsset);
			
			Transaction exchangeTran = new Transaction();
			String exchangeAmount = "";
			if ("BUY".equals(orderType)) {
				exchangeAmount = "" + Double.parseDouble(record.get("Total")) * -1;
				tran.setOrderType(Transaction.OrderType.BUY.name());
			} else if ("SELL".equals(orderType)) {
				exchangeAmount = record.get("Total");
				tran.setOrderType(Transaction.OrderType.SELL.name());
			}
			
			exchangeTran.setDateTime(ldt);
			exchangeTran.setAmount(exchangeAmount, assetOrigine);
			list.add(exchangeTran);
			list.add(tran);
		}
		return list;
	}
}
