package github.aq.cryptoinvestmentwatcher.model;

import java.time.LocalDateTime;

/**
 *
 */
public class Transaction {
	
	public Transaction() {
		id = TransactionIdentifier.computeNextId();
	}
	
	public enum Currency {
		BTC, USD, EUR; //, ETH, ETC, 
	}
	
	public enum OrderType {
		BUY, SELL;
	}
	
	public class AmountCurrency {
		private double amount;
		private Currency currency;
		
		public AmountCurrency(String amount, String currency) {
			
		    try {
		    	this.amount = Double.parseDouble(amount);
		    } catch (NumberFormatException e) {
		    	this.amount = 0d;
		    }
			switch(currency) {
				case "BTC": this.currency = Currency.BTC; break;
				case "USD": this.currency = Currency.USD; break;
				case "EUR": this.currency = Currency.EUR; break;
			}
		}
					
		public double getAmount() {
			return amount;
		}
		
		public Currency getCurrency() {
			return currency;
		}
						
	}
	
	private Website website;
	private String marketType; 
	private LocalDateTime dateTime; // bitstamp format = MMM. DD, YYYY, HH:mm PM/AM
	private String accountId;
	private AmountCurrency amount;
	private AmountCurrency value;
	private AmountCurrency rate;
	private AmountCurrency fee;
	private OrderType orderType;
	private long id;

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public AmountCurrency getAmount() {
		return amount;
	}

	public void setAmount(String amount, String currency) {
		this.amount = new AmountCurrency(amount, currency);
	}
	
	public void setAmount(AmountCurrency amount) {
		this.amount = amount;
	}

	public AmountCurrency getValue() {
		return value;
	}

	public void setValue(AmountCurrency value) {
		this.value = value;
	}

	public void setValue(String amount, String currency) {
		this.value = new AmountCurrency(amount, currency);
	}
	
	public AmountCurrency getRate() {
		return rate;
	}

	public void setRate(AmountCurrency rate) {
		this.rate = rate;
	}

	public void setRate(String amount, String currency) {
		this.rate = new AmountCurrency(amount, currency);
	}
	
	public AmountCurrency getFee() {
		return fee;
	}

	public void setFee(String amount, String currency) {
		this.fee = new AmountCurrency(amount, currency);
	}

	public void setFee(AmountCurrency fee) {
		this.fee = fee;
	}
	
	public String getOrderType() {
		return orderType.name();
	}

	public void setOrderType(String orderType) {
		switch(orderType) {
			case "BUY": this.orderType = OrderType.BUY; break;
			case "SELL": this.orderType = OrderType.SELL; break;
		}
	}
	
	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public static void main(String[] args) {
		

	}

}
