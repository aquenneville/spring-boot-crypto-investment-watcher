package github.aq.cryptoprofittracker.service.impl.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.Transaction;

public class KrakenTradeTransactionsCsvReader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static List<Transaction> read(String filename) {
		List<Transaction> list = new ArrayList<>();
		Iterable<CSVRecord> records = FileReader.read(filename);
		
		for (CSVRecord record : records) {
			Transaction tran = new Transaction();
			 
			// TODO: parse ledger transactions: deposits and withdrawals ?	
			String txId = record.get("txid");	
			tran.setWebsiteTxId(txId);
			tran.setExchange(Exchange.KRAKEN);
			
			String txRefId = record.get("ordertxid");
			tran.setWebsiteTxRefId(txRefId);
			
			String pair = record.get("pair");
			String amount = record.get("vol");
			String newAsset = pair.substring(0, 4);
			String assetOrigine = pair.substring(4);
			tran.setAmount(amount, newAsset);
			
			String type = record.get("type").toUpperCase();
			if (!assetOrigine.startsWith("Z")) {
				// create second transaction i.e XXRPXBT, sell transaction should be added for XXBT
				Transaction exchangeTran = new Transaction();
				exchangeTran.setWebsiteTxRefId(txRefId);
				exchangeTran.setWebsiteTxId(txId);
				exchangeTran.setExchange(Exchange.KRAKEN);
				String amountCost = "";
				if ("BUY".equals(type)) {
					amountCost = "" + (Double.parseDouble(record.get("cost")) * -1);
					exchangeTran.setOrderType(Transaction.OrderType.BUY.name());
				} else if ("SELL".equals(type)) {
					amountCost = record.get("cost");
					exchangeTran.setOrderType(Transaction.OrderType.BUY.name());
				}	
				exchangeTran.setAmount(amountCost, assetOrigine);
				exchangeTran.setOrderType(Transaction.OrderType.BUY.name());
				String fee = record.get("fee");
				exchangeTran.setFee(fee, assetOrigine);
				//sellTran.setRate
				list.add(exchangeTran);
			}
			
			DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS"); //24 hr YYYY-MM-DD HH:mm:ss.SSSS
			String dateTimeValue = record.get("time");
			LocalDateTime ldt = LocalDateTime.parse(dateTimeValue, dTF);
			tran.setDateTime(ldt);
			
			String ordertype = record.get("ordertype");
			tran.setMarketType(ordertype.toUpperCase());
			
			if ("BUY".equals(type)) {
				tran.setOrderType(Transaction.OrderType.BUY.name());
			} else if ("SELL".equals(type)) {
				tran.setOrderType(Transaction.OrderType.SELL.name());
			}
			String price = record.get("price");
			tran.setRate(price, newAsset);
			
		    list.add(tran);
			
			
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
