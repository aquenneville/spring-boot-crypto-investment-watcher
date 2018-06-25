package github.aq.cryptoprofittracker.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import github.aq.cryptoprofittracker.model.Pair;
import github.aq.cryptoprofittracker.model.AssetPortfolio;
import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.ExchangePairs;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/assets/prices")
public class AssetPriceController {

	 @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody AssetPortfolio.PairPrices listAssetPairPrices() {
		 return AssetPortfolio.getAssetPrices();
	 }
	
	//TODO: Add the option to force the update
	// make it more generic by recomposing the assets portfolio and then requesting just the prices needed
    @RequestMapping(path = "/update", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String requestCurrentAssetPairPrices() throws Exception{
    	int pricesFetchedCount = 0;
        if (AssetPortfolio.getLastPriceUpdate() == null) {
        	
        	for (ExchangePairs exchange: AssetPortfolio.getBalances().getAssetPortfolios()) {
        		for (Pair assetPair: exchange.getPairs().keySet()) {
        			CryptowatchResponseCurrentPrice resp = callCryptowatchAssetPairCurrentPrice(exchange.getExchange(), assetPair);
        			AssetPortfolio.getAssetPrices().addPrice(exchange.getExchange(), assetPair, resp.getResult().getPrice());
        			pricesFetchedCount ++;
        		}
        	}
        			
        }
        return "Done - prices fetched count: " + pricesFetchedCount;
    }
    
    public CryptowatchResponseCurrentPrice callCryptowatchAssetPairCurrentPrice(Exchange exchange, Pair assetPair) {
        String uri = "https://api.cryptowat.ch/markets/"+exchange.name().toLowerCase()+"/"+assetPair.name().toLowerCase()+"/price";
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
        return resp;
    }
    
    
}
