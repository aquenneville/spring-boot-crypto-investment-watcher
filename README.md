# spring-boot-crypto-profit-tracker
Aggregate crypto historical transactions data from different exchanges

# Features
- Csv reading compatible with Bitstamp, Kraken and Binance
- Java 8 Streams 
- Spring boot simple security

# Requirements 
1. Install project with git clone
2. Export and download your Csv into the the project's folder under storage/transactions/[exchange name]

# Endpoints
/api/v1/transactions/parse: parses the bitstamp csv file and loads it in memory, calls cryptowatch to get btc current price
/api/v1/transactions/stats: displays details about the trades, total deposits in usd, btc quantity, btc portfolio value in usd, buy and sell order count...

# Todo
- read all files in the transactions/bitstamp folder instead of reading only one file
- implement kraken csv parsing
- implement endpoint to only get transactions per exchange
