package github.aq.cryptoprofittracker.controller;

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
import github.aq.cryptoprofittracker.model.AssetPairPrices;
import github.aq.cryptoprofittracker.model.CryptowatchResponseCurrentPrice;
import github.aq.cryptoprofittracker.model.Trade;
import github.aq.cryptoprofittracker.model.Trades;
import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.Trade.Currency;
import github.aq.cryptoprofittracker.parse.parser.BinanceTradesCsvReader;
import github.aq.cryptoprofittracker.parse.parser.BitstampTradesCsvReader;
import github.aq.cryptoprofittracker.parse.parser.KrakenTradesCsvReader;

@RestController
@RequestMapping("/api/v1/trades")
public class TradesController {

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
		List<Trade> list = parseTransactionsInFolder("storage/trades/bitstamp/bitstamp-account-transactions.csv", Exchange.BITSTAMP);

		
		CryptowatchResponseCurrentPrice bitstampBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Exchange.BITSTAMP, AssetPair.BTCUSD);
		CryptowatchResponseCurrentPrice bitstampBchBtcResp = callCryptowatchAssetPairCurrentPrice(Exchange.BITSTAMP, AssetPair.BCHBTC);
		CryptowatchResponseCurrentPrice krakenBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Exchange.KRAKEN, AssetPair.BTCUSD);
		CryptowatchResponseCurrentPrice binanceBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Exchange.BINANCE, AssetPair.BTCUSDT);
		//CryptowatchResponseCurrentPrice krakenEthUsdResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.ETHUSD);
		//CryptowatchResponseCurrentPrice krakenLtcBtcResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.LTCBTC);
		//CryptowatchResponseCurrentPrice krakenXrpUsdResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.XRPUSD);
		
		AssetPairPrices.addPrice(AssetPair.BTCUSD, Exchange.BITSTAMP, bitstampBtcUsdResp.getResult().getPrice());
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
		Trades.getInstance().getTransactionList().addAll(list);
		return "triggered - count: " + bitstampBtcUsdResp + " " + list.size();
	}
	
	public CryptowatchResponseCurrentPrice callCryptowatchAssetPairCurrentPrice(Exchange website, AssetPair assetPair) {
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
		
		AssetPairPrices.addPrice(assetPair, website, resp.getResult().getPrice());
		return resp;
	}
	
	public List<Trade> parseTransactionsInFolder(final String filename, Exchange website) {		
		switch(website.name()) {
			case "BITSTAMP": return BitstampTradesCsvReader.parse(filename); 
			case "KRAKEN": return KrakenTradesCsvReader.parse(filename); 
			case "BINANCE": return BinanceTradesCsvReader.parse(filename);
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
		
		
		
		Predicate<Trade> predicateTaxYear = null;
		//if (paramTaxYear != null && paramTaxYear > 0) {
		//	LocalDateTime startTaxYearDate = LocalDateTime.parse((paramTaxYear-1)+"-04-06T00:00:00"); // > 04-06
		//	LocalDateTime endTaxYearDate = LocalDateTime.parse(paramTaxYear+"-04-05T24:00:00"); // < 04-06
			
		//	predicateTaxYear = t -> t.getDateTime().isAfter(startTaxYearDate) && t.getDateTime().isBefore(endTaxYearDate);
		//} else {
			predicateTaxYear = t -> true;
		//}
		
		List<Trade> transactionListFiltered = Trades.getInstance().getTransactionList()
				.stream().filter(predicateTaxYear)
				.sorted(Comparator.comparing(Trade::getDateTime))
				.collect(Collectors.toList());				
		
		Predicate<Trade> predicteBuyOrder = t ->  "BUY".equals(t.getOrderType());
		Predicate<Trade> predicteSellOrder = t -> "SELL".equals(t.getOrderType());
		Predicate<Trade> predicteDeposit = t -> (t.getMarketType() != null && "DEPOSIT".equals(t.getMarketType()));
		Predicate<Trade> predicteBistampTransaction = t -> (Exchange.BITSTAMP.equals(t.getWebsite()));
		Predicate<Trade> predicteKrakenTransaction = t -> (Exchange.KRAKEN.equals(t.getWebsite()));
		Predicate<Trade> predicteBinanceTransaction = t -> (Exchange.BINANCE.equals(t.getWebsite()));
		
		long buyOrderCount = transactionListFiltered.stream().filter(predicteBuyOrder).count();
		long sellOrderCount = transactionListFiltered.stream().filter(predicteSellOrder).count();
		long depositCount = transactionListFiltered.stream().filter(predicteDeposit).count();
		long bistampTransactionCount = transactionListFiltered.stream().filter(predicteBistampTransaction).count();
		long krakenTransactionCount = transactionListFiltered.stream().filter(predicteKrakenTransaction).count();
		long binanceTransactionCount = transactionListFiltered.stream().filter(predicteBinanceTransaction).count();
		double depositSum = transactionListFiltered.stream().filter(predicteDeposit).mapToDouble(t -> t.getAmount().getAmount()).sum();
		//btcQty = buyOrderCount - sellOrderCount;
		
		for (Trade t: transactionListFiltered) {
			if ("BUY".equals(t.getOrderType()) && Currency.BTC == t.getAmount().getCurrency()) {
				btcQty += t.getAmount().getAmount();
			} else if ("SELL".equals(t.getOrderType()) && Currency.BTC == t.getAmount().getCurrency()) {
				btcQty -= t.getAmount().getAmount();
			}
		}
		
		double profits = btcQty * AssetPairPrices.getPrice(AssetPair.BTCUSD, Exchange.BITSTAMP);
		
		map.put("deposit-count", (double) depositCount);
		map.put("btc-quantity", btcQty);
		map.put("buy-order-count", (double) buyOrderCount);
		map.put("sell-order-count", (double) sellOrderCount);
		map.put("deposit-sum", depositSum);
		map.put("deposit-count", (double) depositCount);		
		map.put("btc-profits-usd", profits);
		map.put("fees-sum", fees);
		map.put("transactions-count", (double) Trades.getInstance().getTransactionList().size());
		map.put("bitstamp-transactions-count", (double) bistampTransactionCount);
		map.put("kraken-transactions-count", (double) krakenTransactionCount);
		map.put("binance-transactions-count", (double) binanceTransactionCount);
		
		return map;
	}
}

