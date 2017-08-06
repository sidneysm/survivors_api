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
import br.com.sidney.survivor.repository.Survivors;
import br.com.sidney.survivor.repository.Trades;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Trade", tags = "Trades")
public class TradeResource {
	public static final Logger logger = LoggerFactory.getLogger(SurvivorResource.class);

	@Autowired
	private Trades trades;

	@Autowired
	private Survivors survivors;

	// @RequestMapping(value = "/trade", method = RequestMethod.GET)
	// public ResponseEntity<List<Trade>> list() {
	// List<Trade> tradeList = trades.findAll();
	// return new ResponseEntity<List<Trade>>(tradeList, HttpStatus.OK);
	// }

	@RequestMapping(value = "/trade", method = RequestMethod.POST)
	@ApiOperation(value = "Make trade with only survivors marked as non-infected", response = Iterable.class, 
		notes ="Notas")
	public ResponseEntity<?> createTrade(@RequestParam(value = "buyer") Long buyer,
			@RequestParam(value = "seller") Long seller, @RequestBody Inventory inventory) {

		logger.info("Add trade: {}", buyer);

		Survivor survivor_buyer = survivors.findOne(buyer);
		Survivor survivor_seller = survivors.findOne(seller);

		logger.info("Find survivor: {}", survivor_buyer);
		logger.info("Find survivor: {}", survivor_seller);
		Trade trade = new Trade();
		trade.setBuyer(survivor_buyer);
		trade.setSeller(survivor_seller);
		trade.setItems(inventory.getItems());

		boolean t = trade.makeTrade();
		if (!t) {
			return new ResponseEntity<Trade>(HttpStatus.BAD_REQUEST);
		}
		trades.save(trade);
		return new ResponseEntity<>(inventory, HttpStatus.CREATED);
	}
}
