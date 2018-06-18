package github.aq.cryptoinvestmentwatcher.model;

public class AssetPairPrice {
	
	private AssetPair assetPair;
	private Website website;
	private double price;
	
	public AssetPair getAssetPair() {
		return assetPair;
	}
	public void setAssetPair(AssetPair assetPair) {
		this.assetPair = assetPair;
	}
	public Website getWebsite() {
		return website;
	}
	public void setWebsite(Website website) {
		this.website = website;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
