package github.aq.cryptoprofittracker.model;

import java.util.HashMap;
import java.util.Map;

public class ExchangePairs {

    Exchange exchange;
    Map<Pair, Double> pairs;

    public ExchangePairs() {
        pairs = new HashMap<Pair, Double>();
    }
    
    public Exchange getExchange() {
        return exchange;
    }
    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
    public Map<Pair, Double> getPairs() {
        return pairs;
    }
    public void setPairs(Map<Pair, Double> assetPairs) {
        this.pairs = assetPairs;
    }
    
    public void addPrice(Pair assetPair, double price) {
        this.pairs.put(assetPair, price);
    }
    
    public void addQuantity(Pair assetPair, double quantity) {
        this.pairs.put(assetPair, quantity);
    }
}
