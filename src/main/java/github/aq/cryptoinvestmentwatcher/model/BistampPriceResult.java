package github.aq.cryptoinvestmentwatcher.model;

public class BistampPriceResult {

    private Result result;
    private Allowance allowance;
    
    public BistampPriceResult() {
        
    }
    
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

        public Result() { 
        
        }
        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
        
    }
    
    public static class Allowance {
        private long cost;
        private long remaining;
        
        public Allowance() {
            
        }
        
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
}
