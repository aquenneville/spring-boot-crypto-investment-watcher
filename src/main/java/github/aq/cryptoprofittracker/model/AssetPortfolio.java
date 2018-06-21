package github.aq.cryptoprofittracker.model;

import java.time.LocalDate;
import java.util.List;

public class AssetPortfolio {

	static Balances balances;
	static AssetPairPrices assetPrices;
	static LocalDate lastPriceUpdate;
	
	public static Balances getBalances() {
		return balances;
	}
	
	public static AssetPairPrices getAssetPrices() {
		return assetPrices;
	}

	public static LocalDate getLastPriceUpdate() {
		return lastPriceUpdate;
	}
	
	public static void setLastPriceUpdate() {
		lastPriceUpdate = LocalDate.now();
	}
	
	public class Balances {

		List<ExchangeAssetPairs> assetPortfolios;
		
		public List<ExchangeAssetPairs> getAssetPortfolios() {
			return assetPortfolios;
		}
		
		public void addQuantity(Exchange exchange, AssetPair assetPair, double quantity) {
	        int i = 0;
	        boolean addedAssetPairPrice = false;
	        for (ExchangeAssetPairs exchangeAssetPairs: assetPortfolios) {
	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
	                ExchangeAssetPairs eap = assetPortfolios.get(i);
	                eap.addQuantity(assetPair, quantity);
	                addedAssetPairPrice = true;
	                break;
	            } 
	        }
	        if (addedAssetPairPrice) {
	            ExchangeAssetPairs eap = new ExchangeAssetPairs();
	            eap.addQuantity(assetPair, quantity);
	            assetPortfolios.add(eap);
	        }
	    }
	    
	    public double getQuantity(Exchange exchange, AssetPair assetPair) {
	        int i = 0;
	        double quantity = 0;
	        for (ExchangeAssetPairs exchangeAssetPairs: assetPortfolios) {
	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
	                ExchangeAssetPairs eap = assetPortfolios.get(i);
	                if (eap.getAssetPairs().containsKey(assetPair)) {
	                    return eap.getAssetPairs().get(assetPair);
	                }  
	            } 
	        }
	        return quantity;
	    }
	}
	
	public class AssetPairPrices {

		List<ExchangeAssetPairs> list;

	    public void addPrice(Exchange exchange, AssetPair assetPair, double price) {
	        int i = 0;
	        boolean addedAssetPairPrice = false;
	        for (ExchangeAssetPairs exchangeAssetPairs: list) {
	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
	                ExchangeAssetPairs eap = list.get(i);
	                eap.addPrice(assetPair, price);
	                addedAssetPairPrice = true;
	                break;
	            } 
	        }
	        if (addedAssetPairPrice) {
	            ExchangeAssetPairs eap = new ExchangeAssetPairs();
	            eap.addPrice(assetPair, price);
	            list.add(eap);
	        }
	    }
	    
	    public double getPrice(Exchange exchange, AssetPair assetPair) {
	        int i = 0;
	        double price = 0;
	        for (ExchangeAssetPairs exchangeAssetPairs: list) {
	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
	                ExchangeAssetPairs eap = list.get(i);
	                if (eap.getAssetPairs().containsKey(assetPair)) {
	                    return eap.getAssetPairs().get(assetPair);
	                }  
	            } 
	        }
	        return price;
	    }
	}
}
