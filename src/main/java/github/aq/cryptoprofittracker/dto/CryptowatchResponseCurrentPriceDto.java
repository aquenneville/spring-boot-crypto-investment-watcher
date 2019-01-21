package github.aq.cryptoprofittracker.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CryptowatchResponseCurrentPriceDto {

	private Result result;
	private Allowance allowance;
	
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Allowance getAllowance() {
		return allowance;
	}

	public void setAllowance(Allowance allowance) {
		this.allowance = allowance;
	}

	public static class Result {
		private double price;

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}			
	}
	
	static class Allowance {
		private long cost;
		private long remaining;
		
		public long getCost() {
			return cost;
		}
		public void setCost(long cost) {
			this.cost = cost;
		}
		public long getRemaining() {
			return remaining;
		}
		public void setRemaining(long remaining) {
			this.remaining = remaining;
		}		
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();		
		CryptowatchResponseCurrentPriceDto resp = new CryptowatchResponseCurrentPriceDto();
		Result result = new Result();
		result.setPrice(6463.99);
		Allowance allowance = new Allowance();
		allowance.setCost(1);
		allowance.setRemaining(1);
		resp.setResult(result);
		resp.setAllowance(allowance);
		//mapper.writeValue(new File("file.json"), obj);
		System.out.println(mapper.writeValueAsString(resp));
	}

}
