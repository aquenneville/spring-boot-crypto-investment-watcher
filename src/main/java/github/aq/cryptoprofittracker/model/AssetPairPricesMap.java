package github.aq.cryptoprofittracker.model;

import java.util.HashMap;
import java.util.Map;

public class AssetPairPricesMap {

	private static Map<Website, Map<AssetPair, AssetPairPrice>> assetPairPricesMap = new HashMap<Website, Map<AssetPair, AssetPairPrice>>();

	public static Map<Website, Map<AssetPair, AssetPairPrice>> getAssetPairPricesMap() {
		return assetPairPricesMap;
	}

	public static void setAssetPairPricesMap(Map<Website, Map<AssetPair, AssetPairPrice>> assetPairPricesMap) {
		AssetPairPricesMap.assetPairPricesMap = assetPairPricesMap;
	}
	
	public static void addAssetPairPrice(AssetPair assetPair, Website website, Double price) {
		AssetPairPrice assetPairPrice = new AssetPairPrice();
		assetPairPrice.setPrice(price);
		assetPairPrice.setWebsite(website);
		assetPairPrice.setAssetPair(assetPair);
		
		if (!assetPairPricesMap.containsKey(website)) {
			Map<AssetPair, AssetPairPrice> map = new HashMap<>();
			map.put(assetPair, assetPairPrice);
			assetPairPricesMap.put(website, map);
		} else {
			Map<AssetPair, AssetPairPrice> map = assetPairPricesMap.get(website);
			map.put(assetPair, assetPairPrice);
			assetPairPricesMap.put(website, map);			
		}
	}
	
	public static Map<AssetPair, AssetPairPrice> getAssetPairPrice(Website website) {			
		return assetPairPricesMap.get(website);		
	}
}
