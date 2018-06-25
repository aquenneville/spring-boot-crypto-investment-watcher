package github.aq.cryptoprofittracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssetPortfolio {

	static Balances balances;
	static PairPrices assetPrices;
	static LocalDate lastPriceUpdate;
	
	public static Balances getBalances() {
	    if (balances == null) {
	        balances = new AssetPortfolio.Balances();
	    }
		return balances;
	}
	
	public static PairPrices getAssetPrices() {
		return assetPrices;
	}

	public static LocalDate getLastPriceUpdate() {
		return lastPriceUpdate;
	}
	
	public static void setLastPriceUpdate() {
		lastPriceUpdate = LocalDate.now();
	}
	
	public static class Balances {

		List<ExchangePairs> assetBalances;
		
		public Balances() {
		    assetBalances = new ArrayList<ExchangePairs>();
		}
		
		public List<ExchangePairs> getAssetPortfolios() {
			return assetBalances;
		}
		
		public void addBalance(Exchange exchange, Pair assetPair, double quantity) {
	        int i = 0;
	        boolean addedAssetPairPrice = false;
	        if (assetBalances.size() == 0) {
	            ExchangePairs eap = new ExchangePairs();
                eap.addQuantity(assetPair, quantity);
                assetBalances.add(eap);
	        } else {
    	        for (ExchangePairs exchangeAssetPairs: assetBalances) {
    	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
    	                ExchangePairs eap = assetBalances.get(i);
    	                eap.addQuantity(assetPair, quantity);
    	                addedAssetPairPrice = true;
    	                break;
    	            } 
    	        }
    	        
    	        if (addedAssetPairPrice) {
    	            ExchangePairs eap = new ExchangePairs();
    	            eap.addQuantity(assetPair, quantity);
    	            assetBalances.add(eap);
    	        }
	        }
	    }
	    
	    public double getBalance(Exchange exchange, Pair assetPair) {
	        int i = 0;
	        double balance = 0;
	        if (assetBalances == null) {
                assetBalances = new ArrayList<ExchangePairs>();
            }
	        for (ExchangePairs exchangeAssetPairs: assetBalances) {
	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
	                ExchangePairs eap = assetBalances.get(i);
	                if (eap.getPairs().containsKey(assetPair)) {
	                    return eap.getPairs().get(assetPair);
	                }  
	            } 
	        }
	        return balance;
	    }
	}
	
	public class PairPrices {

		List<ExchangePairs> pairs;

	    public void addPrice(Exchange exchange, Pair assetPair, double price) {
	        int i = 0;
	        boolean addedAssetPairPrice = false;
	        for (ExchangePairs exchangeAssetPairs: pairs) {
	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
	                ExchangePairs eap = pairs.get(i);
	                eap.addPrice(assetPair, price);
	                addedAssetPairPrice = true;
	                break;
	            } 
	        }
	        if (addedAssetPairPrice) {
	            ExchangePairs eap = new ExchangePairs();
	            eap.addPrice(assetPair, price);
	            pairs.add(eap);
	        }
	    }
	    
	    public double getPrice(Exchange exchange, Pair assetPair) {
	        int i = 0;
	        double price = 0;
	        for (ExchangePairs exchangeAssetPairs: pairs) {
	            if (exchange.equals(exchangeAssetPairs.getExchange())) {
	                ExchangePairs eap = pairs.get(i);
	                if (eap.getPairs().containsKey(assetPair)) {
	                    return eap.getPairs().get(assetPair);
	                }  
	            } 
	        }
	        return price;
	    }
	}
}
