package github.aq.cryptoprofittracker.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import github.aq.cryptoprofittracker.model.AssetPair;
import github.aq.cryptoprofittracker.model.AssetPairPrices;
import github.aq.cryptoprofittracker.model.CryptowatchResponseCurrentPrice;
import github.aq.cryptoprofittracker.model.Exchange;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/trades/prices")
public class AssetPairPriceController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String requestCurrentAssetPairPrices() throws Exception{
        CryptowatchResponseCurrentPrice bitstampBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Exchange.BITSTAMP, AssetPair.BTCUSD);
        CryptowatchResponseCurrentPrice bitstampBchBtcResp = callCryptowatchAssetPairCurrentPrice(Exchange.BITSTAMP, AssetPair.BCHBTC);
        CryptowatchResponseCurrentPrice krakenBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Exchange.KRAKEN, AssetPair.BTCUSD);
        CryptowatchResponseCurrentPrice binanceBtcUsdResp = callCryptowatchAssetPairCurrentPrice(Exchange.BINANCE, AssetPair.BTCUSDT);
        //CryptowatchResponseCurrentPrice krakenEthUsdResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.ETHUSD);
        //CryptowatchResponseCurrentPrice krakenLtcBtcResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.LTCBTC);
        //CryptowatchResponseCurrentPrice krakenXrpUsdResp = callCryptowatchAssetPairCurrentPrice(Website.KRAKEN, AssetPair.XRPUSD);
        
        AssetPairPrices.addPrice(AssetPair.BTCUSD, Exchange.BITSTAMP, bitstampBtcUsdResp.getResult().getPrice());
        //AssetPairPricesMap.addAssetPairPrice(AssetPair.BCHUSD, Website.BITSTAMP, bitstampBchBtcResp.getResult().getPrice());
        //AssetPairPricesMap.addAssetPairPrice(AssetPair.BTCUSD, Website.KRAKEN, krakenBtcUsdResp.getResult().getPrice());
        //AssetPairPricesMap.addAssetPairPrice(AssetPair.BTCUSD, Website.BINANCE, binanceBtcUsdResp.getResult().getPrice());
        //AssetPairPricesMap.addAssetPairPrice(AssetPair.ETHUSD, Website.KRAKEN, krakenEthUsdResp.getResult().getPrice());
        //AssetPairPricesMap.addAssetPairPrice(AssetPair.LTCBTC, Website.KRAKEN, krakenLtcBtcResp.getResult().getPrice());
        //AssetPairPricesMap.addAssetPairPrice(AssetPair.XRPUSD, Website.KRAKEN, krakenXrpUsdResp.getResult().getPrice());
        return "Done";
    }
    
    public CryptowatchResponseCurrentPrice callCryptowatchAssetPairCurrentPrice(Exchange website, AssetPair assetPair) {
        String uri = "https://api.cryptowat.ch/markets/"+website.name().toLowerCase()+"/"+assetPair.name().toLowerCase()+"/price";
        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //connection.setRequestProperty("Accept", "application/xml");
        ObjectMapper mapper = new ObjectMapper();   
        CryptowatchResponseCurrentPrice resp = null;
        try {
            resp = mapper.readValue(connection.getInputStream(), CryptowatchResponseCurrentPrice.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
        connection.disconnect();
        
        AssetPairPrices.addPrice(assetPair, website, resp.getResult().getPrice());
        return resp;
    }
}
