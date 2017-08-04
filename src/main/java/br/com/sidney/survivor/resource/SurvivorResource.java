package br.com.sidney.survivor.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sidney.survivor.model.Location;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.repository.Survivors;

@RestController
public class SurvivorResource {

	public static final Logger logger = LoggerFactory.getLogger(SurvivorResource.class);

	@Autowired
	private Survivors survivors;

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

	@RequestMapping(value = "/survivors/{id}/infected", method = RequestMethod.PUT)
	public ResponseEntity<?> infected(@PathVariable("id") Long id) {
		Survivor survivor = survivors.findOne(id);
		if (survivor == null) {
			return new ResponseEntity<Survivor>(HttpStatus.NOT_FOUND);
		}
		survivor.setInfected(true);
		survivors.save(survivor);
		return new ResponseEntity<>(survivor, HttpStatus.OK);
	}

	@RequestMapping(value = "/survivors/{id}/location", method = RequestMethod.PUT)
	public ResponseEntity<?> newLocation(@PathVariable("id") Long id, @RequestBody Location location) {
		Survivor survivor = survivors.findOne(id);
		logger.info("update location: {}", location);
		if (survivor == null) {
			logger.info("não encontrado");
			return new ResponseEntity<Survivor>(HttpStatus.NOT_FOUND);
		}
		survivor.setLastLocation(location);
		survivors.save(survivor);
		return new ResponseEntity<>(survivor, HttpStatus.OK);
	}

}
