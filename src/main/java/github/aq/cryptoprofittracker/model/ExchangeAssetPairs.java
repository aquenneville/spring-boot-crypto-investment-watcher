package github.aq.cryptoprofittracker.model;

import java.util.Map;

public class ExchangeAssetPairs {

    Exchange exchange;
    Map<AssetPair, Double> assetPairs;
    
    public Exchange getExchange() {
        return exchange;
    }
    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
    public Map<AssetPair, Double> getAssetPairs() {
        return assetPairs;
    }
    public void setAssetPairs(Map<AssetPair, Double> assetPairs) {
        this.assetPairs = assetPairs;
    }
    
    public void addPrice(AssetPair assetPair, double price) {
        this.assetPairs.put(assetPair, price);
    }
    
    public void addQuantity(AssetPair assetPair, double quantity) {
        this.assetPairs.put(assetPair, quantity);
    }
}
