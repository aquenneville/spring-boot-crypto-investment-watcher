package github.aq.cryptoprofittracker.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import github.aq.cryptoprofittracker.model.AssetPortfolio;

@RestController
@RequestMapping("/api/v1/assets/portfolio")
public class AssetPortfolioController {

	 @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody AssetPortfolio.Balances listAssetsInPortfolio() {
		 return AssetPortfolio.getBalances();
	 }
}
