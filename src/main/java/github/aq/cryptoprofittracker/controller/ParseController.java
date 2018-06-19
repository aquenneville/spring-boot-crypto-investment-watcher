package github.aq.cryptoprofittracker.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
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
import github.aq.cryptoprofittracker.model.AssetPairPricesMap;
import github.aq.cryptoprofittracker.model.CryptowatchResponseCurrentPrice;
import github.aq.cryptoprofittracker.model.Transaction;
import github.aq.cryptoprofittracker.model.Transactions;
import github.aq.cryptoprofittracker.model.Website;
import github.aq.cryptoprofittracker.model.Transaction.Currency;
import github.aq.cryptoprofittracker.parse.parser.BinanceTransactionsCsvParser;
import github.aq.cryptoprofittracker.parse.parser.BitstampTransactionsCsvParser;
import github.aq.cryptoprofittracker.parse.parser.KrakenTransactionsCsvParser;

@RestController
@RequestMapping("/api/v1/transactions")
public class ParseController {

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
		long start = System.currentTimeMillis();		
		List<Transaction> list = parseTransactionsInFolder("storage/transactions/bitstamp/bitstamp-account-transactions.csv", Website.BITSTAMP);

		
		CryptowatchResponseCurrentPrice bitstampBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Website.BITSTAMP, AssetPair.BTCUSD);
		CryptowatchResponseCurrentPrice bitstampBchBtcResp = callCryptowatchAssetPairCurrentPrice(Website.BITSTAMP, AssetPair.BCHBTC);
		CryptowatchResponseCurrentPrice krakenBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.BTCUSD);
		//CryptowatchResponseCurrentPrice binanceBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Website.BINANCE, AssetPair.BTCUSD);
		//CryptowatchResponseCurrentPrice krakenEthUsdResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.ETHUSD);
		//CryptowatchResponseCurrentPrice krakenLtcBtcResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.LTCBTC);
		//CryptowatchResponseCurrentPrice krakenXrpUsdResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.XRPUSD);
		
		AssetPairPricesMap.addAssetPairPrice(AssetPair.BTCUSD, Website.BITSTAMP, bitstampBtcUsdResp.getResult().getPrice());
		//AssetPairPricesMap.addAssetPairPrice(AssetPair.BCHUSD, Website.BITSTAMP, bitstampBchBtcResp.getResult().getPrice());
		//AssetPairPricesMap.addAssetPairPrice(AssetPair.BTCUSD, Website.KRAKEN, krakenBtcUsdResp.getResult().getPrice());
		//AssetPairPricesMap.addAssetPairPrice(AssetPair.BTCUSD, Website.BINANCE, binanceBtcUsdResp.getResult().getPrice());
		//AssetPairPricesMap.addAssetPairPrice(AssetPair.ETHUSD, Website.KRAKEN, krakenEthUsdResp.getResult().getPrice());
		//AssetPairPricesMap.addAssetPairPrice(AssetPair.LTCBTC, Website.KRAKEN, krakenLtcBtcResp.getResult().getPrice());
		//AssetPairPricesMap.addAssetPairPrice(AssetPair.XRPUSD, Website.KRAKEN, krakenXrpUsdResp.getResult().getPrice());
//		String uri =
//			    "https://api.cryptowat.ch/markets/bitstamp/btcusd/price";
//		URL url = new URL(uri);
//		HttpURLConnection connection =
//		    (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("GET");
//		//connection.setRequestProperty("Accept", "application/xml");
//		ObjectMapper mapper = new ObjectMapper();	
//		CryptowatchResponseCurrentPrice resp = mapper.readValue(connection.getInputStream(), CryptowatchResponseCurrentPrice.class);		
//		connection.disconnect();
//		
//		AssetPairPricesMap.addAssetPairPrice(AssetPair.BTCUSD, Website.BITSTAMP, resp.getResult().getPrice());
		
		long end = System.currentTimeMillis() - start;
		System.out.println("ms:" + end + " bitstamp: AssetPair.BTCUSD: " + bitstampBtcUsdResp.getResult().getPrice());
		Transactions.getInstance().getTransactionList().addAll(list);
		return "triggered - count: " + bitstampBtcUsdResp + " " + list.size();
	}
	
	public CryptowatchResponseCurrentPrice callCryptowatchAssetPairCurrentPrice(Website website, AssetPair assetPair) {
		String uri = "https://api.cryptowat.ch/markets/"+website.name().toLowerCase()+"/"+assetPair.name().toLowerCase()+"/price";
		URL url = null;
		try {
			url = new URL(uri);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//connection.setRequestProperty("Accept", "application/xml");
		ObjectMapper mapper = new ObjectMapper();	
		CryptowatchResponseCurrentPrice resp = null;
		try {
			resp = mapper.readValue(connection.getInputStream(), CryptowatchResponseCurrentPrice.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		connection.disconnect();
		
		AssetPairPricesMap.addAssetPairPrice(assetPair, website, resp.getResult().getPrice());
		return resp;
	}
	
	public List<Transaction> parseTransactionsInFolder(final String filename, Website website) {		
		switch(website.name()) {
			case "BITSTAMP": return BitstampTransactionsCsvParser.parse(filename); 
			case "KRAKEN": return KrakenTransactionsCsvParser.parse(filename); 
			case "BINANCE": return BinanceTransactionsCsvParser.parse(filename);
		}
	    return null; 
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
		Predicate<Transaction> predicteBistampTransaction = t -> (Website.BITSTAMP.equals(t.getWebsite()));
		Predicate<Transaction> predicteKrakenTransaction = t -> (Website.KRAKEN.equals(t.getWebsite()));
		Predicate<Transaction> predicteBinanceTransaction = t -> (Website.BINANCE.equals(t.getWebsite()));
		
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
		
		double profits = btcQty * AssetPairPricesMap.getAssetPairPrice(Website.BITSTAMP).get(AssetPair.BTCUSD).getPrice();
		
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

