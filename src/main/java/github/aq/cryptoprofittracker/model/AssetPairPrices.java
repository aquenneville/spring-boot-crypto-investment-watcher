package github.aq.cryptoprofittracker.model;

import java.util.List;

public class AssetPairPrices {

	static List<ExchangeAssetPairs> list;

    public static void addPrice(AssetPair assetPair, Exchange exchange, double price) {
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
    
    public static double getPrice(AssetPair assetPair, Exchange exchange) {
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
