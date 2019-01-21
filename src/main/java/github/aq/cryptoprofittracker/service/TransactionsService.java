package github.aq.cryptoprofittracker.service;

public interface TransactionsService {

    void readTransactions(String exchange);

    void readTransactions();

    void stats();

    void statsMarketType(Boolean isTaxYear, int year, String marketType);

    void statsAssetYear(Boolean isTaxYear, int year, String asset);
}
