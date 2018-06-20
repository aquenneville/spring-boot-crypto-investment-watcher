package github.aq.cryptoprofittracker.model;

import java.util.List;

public class Balances {

	static List<ExchangeAssetPairs> assetPortfolios;
	
//	public class AssetPortfolio {
//		Exchange website;
//		Map<AssetPair, Double> assets;
//	}
	
	public static void addQuantity(AssetPair assetPair, Exchange exchange, double quantity) {
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
    
    public static double getQuantity(AssetPair assetPair, Exchange exchange) {
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
