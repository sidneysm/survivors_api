package br.com.sidney.survivor.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sidney.survivor.model.Inventory;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.model.Trade;
import br.com.sidney.survivor.repository.SurvivorsRepository;
import br.com.sidney.survivor.repository.TradesRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Trade", tags = "Trades")
public class TradeResource {
	public static final Logger logger = LoggerFactory.getLogger(SurvivorResource.class);

	@Autowired
	private TradesRepository trades;

	@Autowired
	private SurvivorsRepository survivors;

	public TradeResource(TradesRepository tradesRepository, SurvivorsRepository survivors) {
		this.trades = tradesRepository;
		this.survivors = survivors;
	}

	@RequestMapping(value = "/trades", method = RequestMethod.POST)
	@ApiOperation(value = "Make trade with only survivors marked as non-infected", response = Trade.class, 
		notes ="Pass the id's (buyer and seller) in the query params, "
				+ "and the itens have pass in the request body")
	public ResponseEntity<?> createTrade(@RequestParam(value = "buyer") Long buyer,
			@RequestParam(value = "seller") Long seller, @RequestBody Inventory inventory) {
		Trade trade = new Trade();
		logger.info("Add trade: {}", buyer);
		
		Survivor survivor_buyer;
		try {
			survivor_buyer = survivors.findOne(buyer);
			logger.info("Found survivor: {}", survivor_buyer);
			trade.setBuyer(survivor_buyer);
			
			Survivor survivor_seller = survivors.findOne(seller);
			logger.info("Found survivor: {}", survivor_seller);
			
			
			trade.setSeller(survivor_seller);
			trade.setItems(inventory.getItems());

		} catch (Exception e) {
			
			logger.info("Deu Erro: {}", seller);
			logger.info("Deu Erro: {}", survivors);
			return new ResponseEntity<Trade>(HttpStatus.NOT_FOUND);
		}
		
		boolean t = trade.makeTrade();
		if (!t) {
			return new ResponseEntity<Trade>(HttpStatus.BAD_REQUEST);
		}
		trades.save(trade);
		return new ResponseEntity<>(trade, HttpStatus.CREATED);
	}
}
