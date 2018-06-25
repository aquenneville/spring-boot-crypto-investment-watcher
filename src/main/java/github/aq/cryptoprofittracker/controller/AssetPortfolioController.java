package github.aq.cryptoprofittracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import github.aq.cryptoprofittracker.model.AssetPortfolio;
import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.Pair;

@RestController
@RequestMapping("/api/v1/assets/portfolio")
public class AssetPortfolioController {

	 @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody Map<String, Object> listAssetsInPortfolio() {
	     Map<String, Object> map = new HashMap<String, Object>();
	     map.put("balances", AssetPortfolio.getBalances());
		 return map;
	 }
	 
	 @RequestMapping(path = "/value", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody Map<String, Object> displayPortfolioValue() {
         Map<String, Object> map = new HashMap<String, Object>();
         double btcValueInUsd = 0;
         double btcBalance = AssetPortfolio.getBalances().getBalance(Exchange.BITSTAMP, Pair.BTCUSD);
         if (AssetPortfolio.getAssetPrices() != null) {
             btcValueInUsd = btcBalance * AssetPortfolio.getAssetPrices().getPrice(Exchange.BITSTAMP, Pair.BTCUSD);
         }
         map.put("btc-value-in-usd", btcValueInUsd);
         return map;
     }
}
