package github.aq.cryptoprofittracker.service.impl;

import github.aq.cryptoprofittracker.model.AssetPortfolio;
import github.aq.cryptoprofittracker.model.Exchange;
import github.aq.cryptoprofittracker.model.Pair;
import github.aq.cryptoprofittracker.service.AssetPortfolioService;
import org.springframework.stereotype.Service;

@Service
public class AssetPortfolioServiceImpl implements AssetPortfolioService {

    @Override
    public double computeAssetPortfolioBtcValueInUsd() {
        double btcValueInUsd = 0;
        double btcBalance = AssetPortfolio.getBalances().getBalance(Exchange.BITSTAMP, Pair.BTCUSD);
        if (AssetPortfolio.getAssetPrices() != null) {
            btcValueInUsd = btcBalance * AssetPortfolio.getAssetPrices().getPrice(Exchange.BITSTAMP, Pair.BTCUSD);
        }
        return btcValueInUsd;
    }
}
