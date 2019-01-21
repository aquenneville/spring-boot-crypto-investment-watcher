package github.aq.cryptoprofittracker.web;

import java.util.HashMap;
import java.util.Map;

import github.aq.cryptoprofittracker.service.AssetPortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import github.aq.cryptoprofittracker.model.AssetPortfolio;
import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.Pair;


@RestController
@RequestMapping("/api/v1/assets/portfolio")
public class AssetPortfolioResource {

    @Autowired
    AssetPortfolioService assetPortfolioService;

	 @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Map<String, Object>> listAssetsInPortfolio() {
	     Map<String, Object> map = new HashMap<String, Object>();
	     map.put("balances", AssetPortfolio.getBalances());
		 return new ResponseEntity<>(map, HttpStatus.OK);
	 }
	 
	 @RequestMapping(path = "/value", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Map<String, Object>> displayPortfolioValue() {
         Map<String, Object> map = new HashMap<String, Object>();
         double btcValueInUsd = 0;
         double btcBalance = AssetPortfolio.getBalances().getBalance(Exchange.BITSTAMP, Pair.BTCUSD);
         if (AssetPortfolio.getAssetPrices() != null) {
             btcValueInUsd = btcBalance * AssetPortfolio.getAssetPrices().getPrice(Exchange.BITSTAMP, Pair.BTCUSD);
         }
         map.put("btc-value-in-usd", btcValueInUsd);
         return new ResponseEntity<>(map, HttpStatus.OK);
     }
}
