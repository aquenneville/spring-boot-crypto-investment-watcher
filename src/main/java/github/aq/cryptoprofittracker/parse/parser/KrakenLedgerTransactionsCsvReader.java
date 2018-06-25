package github.aq.cryptoprofittracker.parse.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.Transaction;

public class KrakenLedgerTransactionsCsvReader {
	
	public static List<Transaction> read(String filename) {
		
		List<Transaction> list = new ArrayList<>();
		Iterable<CSVRecord> records = FileReader.read(filename);
		
		for (CSVRecord record : records) {
			Transaction tran = new Transaction();
			 
			// TODO: parse ledger transactions: deposits and withdrawals ?	
			String txId = record.get("txid");
			if (!"".equals(txId)) {
				tran.setWebsiteTxId(txId);
				tran.setExchange(Exchange.KRAKEN);
				
				String txRefId = record.get("refid");
				tran.setWebsiteTxRefId(txRefId);
				
				DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //24 hr YYYY-MM-DD HH:mm:ss

				String dateTimeValue = record.get("time");
				LocalDateTime ldt = LocalDateTime.parse(dateTimeValue, dTF);
				tran.setDateTime(ldt);
				
				String aclass = record.get("aclass");
				
				String type = record.get("type");
				tran.setMarketType(type.toUpperCase());
				
				String assetCode = record.get("asset");
				String amount = record.get("amount");
				String convertedAssetCode = mapToAppAssetCode(assetCode);
				tran.setAmount(amount, convertedAssetCode);
				
				String fee = record.get("fee");
				tran.setAmount(fee, convertedAssetCode);
				
			    list.add(tran);
			}
			
		}
		return list;
	}
	
	private static String mapToAppAssetCode(String currencyCode) {
		String convertedCurrencyCode = "";
		switch(currencyCode) {
		case "XXBT": convertedCurrencyCode = Transaction.Currency.BTC.name(); break; 
		case "XXRP": convertedCurrencyCode = Transaction.Currency.XRP.name(); break;
		case "ZGBP": convertedCurrencyCode = Transaction.Currency.GBP.name(); break;
		case "ZUSD": convertedCurrencyCode = Transaction.Currency.USD.name(); break;
		case "XXLM": convertedCurrencyCode = Transaction.Currency.STR.name(); break;
		case "BCH": convertedCurrencyCode = Transaction.Currency.BCH.name(); break;
		case "XLTC": convertedCurrencyCode = Transaction.Currency.LTC.name(); break;
		case "XMR": convertedCurrencyCode = Transaction.Currency.XMR.name(); break;
		case "ZEUR": convertedCurrencyCode = Transaction.Currency.EUR.name(); break;
		}
		return convertedCurrencyCode;
	}
}
