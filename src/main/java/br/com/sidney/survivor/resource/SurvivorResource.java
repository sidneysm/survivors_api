package br.com.sidney.survivor.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sidney.survivor.model.Inventory;
import br.com.sidney.survivor.model.ItemEnum;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.repository.Survivors;

@RestController
public class SurvivorResource {

	public static final Logger logger = LoggerFactory.getLogger(SurvivorResource.class);

	@Autowired
	private Survivors survivors;
	
	private Map<Integer, Survivor> surv = new HashMap<Integer, Survivor>();
	
	@RequestMapping(value = "/survivors", method = RequestMethod.GET)
	public ResponseEntity<List<Survivor>> list() {
		List<Survivor> survivorsList = survivors.findAll();
		return new ResponseEntity<List<Survivor>>(survivorsList, HttpStatus.OK);
	}

	@RequestMapping(value = "/survivors", method = RequestMethod.POST)
	public ResponseEntity<?> addSurvivor(@RequestBody Survivor survivor) {
		logger.info("Add survivor: {}", survivor);

		survivors.save(survivor);
		return new ResponseEntity<>(survivor, HttpStatus.CREATED);
	}
}
