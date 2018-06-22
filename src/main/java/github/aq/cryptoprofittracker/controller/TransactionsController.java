package github.aq.cryptoprofittracker.controller;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import github.aq.cryptoprofittracker.model.AssetPair;
import github.aq.cryptoprofittracker.model.AssetPortfolio;
import github.aq.cryptoprofittracker.model.Transaction;
import github.aq.cryptoprofittracker.model.Transactions;
import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.Transaction.Currency;
import github.aq.cryptoprofittracker.parse.parser.BinanceTransactionsCsvReader;
import github.aq.cryptoprofittracker.parse.parser.BitstampTransactionsCsvReader;
import github.aq.cryptoprofittracker.parse.parser.KrakenLedgerTransactionsCsvReader;
import github.aq.cryptoprofittracker.parse.parser.KrakenTradeTransactionsCsvReader;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionsController {

	// /api/v1/parse/transactions
	// portfolio value
	// fees 
	// profits
	// deposits
	// fees by exchange
	// deposits by exchange
	// profits by exchange
	// profits by currency
	// profits by year by exchange
	//@RequestMapping(path = "/{saleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	//public @ResponseBody Sale getById(@PathVariable Long saleId) {
	
	// TODO: rewrite the try-catch
	@RequestMapping(path = "/parse", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String parseAll() throws Exception{		
		List<Transaction> list = parseTransactionsInFolder("storage/transactions/bitstamp/", Exchange.BITSTAMP);
		Transactions.getInstance().getTransactionList().addAll(list);
		list = parseTransactionsInFolder("storage/transactions/kraken/ledgers/", Exchange.KRAKEN);
		Transactions.getInstance().getTransactionList().addAll(list);
		list = parseTransactionsInFolder("storage/transactions/kraken/trades/", Exchange.KRAKEN);
		Transactions.getInstance().getTransactionList().addAll(list);
		
		return "triggered - count: " + Transactions.getInstance().getTransactionList().size();
	}
	
	public List<Transaction> parseTransactionsInFolder(final String folder, Exchange website) {		
		File inFolder = new File(folder);
		List<Transaction> transactionHistory = null;
		for (final File fileEntry: inFolder.listFiles()) {
			List<Transaction> transactions = null;
			if (fileEntry.isFile()) {
				
				switch(website.name()) {
					case "BITSTAMP": transactions = BitstampTransactionsCsvReader.read(fileEntry.getAbsolutePath()); 
					break;
					case "KRAKEN": 
						transactions = folder.endsWith("ledgers/") ? 
								KrakenLedgerTransactionsCsvReader.read(fileEntry.getAbsolutePath()):
								KrakenTradeTransactionsCsvReader.read(fileEntry.getAbsolutePath()); 
						break;
					case "BINANCE": transactions = BinanceTransactionsCsvReader.read(fileEntry.getAbsolutePath()); break;
				}
			}
			if (transactions != null) {
				transactionHistory.addAll(transactions);
			}
		}
	    return transactionHistory; 
	}
	
	
	// deposits
	@RequestMapping(path = "/stats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody Map<String, Double> computeProfit(@RequestParam(value = "tax-year", required = false) Integer paramTaxYear) { 
		
		Map<String, Double> map = new HashMap<String, Double>();
		double btcQty = 0;
		//double depositSum = 0;
		// filter out period: 2016-2017
		// filter out assets: btc, eth
		double fees = 0;
//		int depositCount = 0;
//		int sellOrderCount = 0;
//		int buyOrderCount = 0;
		
		
		
		Predicate<Transaction> predicateTaxYear = null;
		//if (paramTaxYear != null && paramTaxYear > 0) {
		//	LocalDateTime startTaxYearDate = LocalDateTime.parse((paramTaxYear-1)+"-04-06T00:00:00"); // > 04-06
		//	LocalDateTime endTaxYearDate = LocalDateTime.parse(paramTaxYear+"-04-05T24:00:00"); // < 04-06
			
		//	predicateTaxYear = t -> t.getDateTime().isAfter(startTaxYearDate) && t.getDateTime().isBefore(endTaxYearDate);
		//} else {
			predicateTaxYear = t -> true;
		//}
		
		List<Transaction> transactionListFiltered = Transactions.getInstance().getTransactionList()
				.stream().filter(predicateTaxYear)
				.sorted(Comparator.comparing(Transaction::getDateTime))
				.collect(Collectors.toList());				
		
		Predicate<Transaction> predicteBuyOrder = t ->  "BUY".equals(t.getOrderType());
		Predicate<Transaction> predicteSellOrder = t -> "SELL".equals(t.getOrderType());
		Predicate<Transaction> predicteDeposit = t -> (t.getMarketType() != null && "DEPOSIT".equals(t.getMarketType()));
		Predicate<Transaction> predicteBistampTransaction = t -> (Exchange.BITSTAMP.equals(t.getExchange()));
		Predicate<Transaction> predicteKrakenTransaction = t -> (Exchange.KRAKEN.equals(t.getExchange()));
		Predicate<Transaction> predicteBinanceTransaction = t -> (Exchange.BINANCE.equals(t.getExchange()));
		
		long buyOrderCount = transactionListFiltered.stream().filter(predicteBuyOrder).count();
		long sellOrderCount = transactionListFiltered.stream().filter(predicteSellOrder).count();
		long depositCount = transactionListFiltered.stream().filter(predicteDeposit).count();
		long bistampTransactionCount = transactionListFiltered.stream().filter(predicteBistampTransaction).count();
		long krakenTransactionCount = transactionListFiltered.stream().filter(predicteKrakenTransaction).count();
		long binanceTransactionCount = transactionListFiltered.stream().filter(predicteBinanceTransaction).count();
		double depositSum = transactionListFiltered.stream().filter(predicteDeposit).mapToDouble(t -> t.getAmount().getAmount()).sum();
		//btcQty = buyOrderCount - sellOrderCount;
		
		for (Transaction t: transactionListFiltered) {
			if ("BUY".equals(t.getOrderType()) && Currency.BTC == t.getAmount().getCurrency()) {
				btcQty += t.getAmount().getAmount();
			} else if ("SELL".equals(t.getOrderType()) && Currency.BTC == t.getAmount().getCurrency()) {
				btcQty -= t.getAmount().getAmount();
			}
		}
		
		double profits = btcQty * AssetPortfolio.getAssetPrices().getPrice(Exchange.BITSTAMP, AssetPair.BTCUSD);
		
		map.put("deposit-count", (double) depositCount);
		map.put("btc-quantity", btcQty);
		map.put("buy-order-count", (double) buyOrderCount);
		map.put("sell-order-count", (double) sellOrderCount);
		map.put("deposit-sum", depositSum);
		map.put("deposit-count", (double) depositCount);		
		map.put("btc-profits-usd", profits);
		map.put("fees-sum", fees);
		map.put("transactions-count", (double) Transactions.getInstance().getTransactionList().size());
		map.put("bitstamp-transactions-count", (double) bistampTransactionCount);
		map.put("kraken-transactions-count", (double) krakenTransactionCount);
		map.put("binance-transactions-count", (double) binanceTransactionCount);
		
		return map;
	}
}

