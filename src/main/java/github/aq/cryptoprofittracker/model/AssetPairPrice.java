package github.aq.cryptoprofittracker.model;

public class AssetPairPrice {
	
	private AssetPair assetPair;
	private Exchange website;
	private double price;
	
	public AssetPair getAssetPair() {
		return assetPair;
	}
	public void setAssetPair(AssetPair assetPair) {
		this.assetPair = assetPair;
	}
	public Exchange getWebsite() {
		return website;
	}
	public void setWebsite(Exchange website) {
		this.website = website;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
