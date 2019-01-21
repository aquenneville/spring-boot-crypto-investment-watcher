package github.aq.cryptoprofittracker.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.aq.cryptoprofittracker.dto.CryptowatchResponseCurrentPriceDto;
import github.aq.cryptoprofittracker.model.AssetPortfolio;
import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.ExchangePairs;
import github.aq.cryptoprofittracker.model.Pair;
import github.aq.cryptoprofittracker.service.AssetPriceService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Service
public class AssetPriceServiceImpl implements AssetPriceService {
    @Override
    public void getLastPrice() {
        int pricesFetchedCount = 0;
        if (AssetPortfolio.getLastPriceUpdate() == null) {

            for (ExchangePairs exchange: AssetPortfolio.getBalances().getAssetPortfolios()) {
                for (Pair assetPair: exchange.getPairs().keySet()) {
                    CryptowatchResponseCurrentPriceDto resp = callCryptowatchAssetPairCurrentPrice(exchange.getExchange(), assetPair);
                    AssetPortfolio.getAssetPrices().addPrice(exchange.getExchange(), assetPair, resp.getResult().getPrice());
                    pricesFetchedCount ++;
                }
            }

        }
    }

    public CryptowatchResponseCurrentPriceDto callCryptowatchAssetPairCurrentPrice(Exchange exchange, Pair assetPair) {
        String uri = "https://api.cryptowat.ch/markets/"+exchange.name().toLowerCase()+"/"+assetPair.name().toLowerCase()+"/price";
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
        CryptowatchResponseCurrentPriceDto resp = null;
        try {
            resp = mapper.readValue(connection.getInputStream(), CryptowatchResponseCurrentPriceDto.class);
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
        return resp;
    }
}
