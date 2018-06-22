package github.aq.cryptoprofittracker.model;

public class AssetPairPrice {
	
	private AssetPair assetPair;
	private Exchange exchange;
	private double price;
	
	public AssetPair getAssetPair() {
		return assetPair;
	}
	public void setAssetPair(AssetPair assetPair) {
		this.assetPair = assetPair;
	}
	public Exchange getExchange() {
		return exchange;
	}
	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
