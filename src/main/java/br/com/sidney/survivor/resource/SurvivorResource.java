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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/survivors")
@Api(value="Suvivor", tags="Survivors")
public class SurvivorResource {

	public static final Logger logger = LoggerFactory.getLogger(SurvivorResource.class);

	
	@Autowired
	private Survivors survivors;

	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation(value = "List all survivors", response = Iterable.class, 
			notes = "Return a list with all survivors")
	public ResponseEntity<List<Survivor>> list() {
		List<Survivor> survivorsList = survivors.findAll();
		return new ResponseEntity<List<Survivor>>(survivorsList, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "Successfully add survivor")
		}
	)
	@ApiOperation(value = "Add a new survivor", response = Survivor.class,
			notes = "Pass a json format survivor, like showed below to add a new survivor in data base")
	public ResponseEntity<?> addSurvivor(@RequestBody Survivor survivor) {
		logger.info("Add survivor: {}", survivor);

		survivors.save(survivor);
		return new ResponseEntity<>(survivor, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/survivors/{id}/infected", method = RequestMethod.PUT)
	@ApiOperation(value = "Mark a survivor as infected", response = Survivor.class,
			notes = "Pass the id survivor to mark him/her as infected")
	@ApiResponses(value = {
	        @ApiResponse(code = 404, message = "Suvivor not found"),
	        @ApiResponse(code = 200, message = "Successfully mark survivor as infected")
		}
	)
	public ResponseEntity<?> infected(@PathVariable("id") Long id) {
		Survivor survivor = survivors.findOne(id);
		if (survivor == null) {
			return new ResponseEntity<Survivor>(HttpStatus.NOT_FOUND);
		}
		survivor.setInfected(true);
		survivors.save(survivor);
		return new ResponseEntity<>(survivor, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/location", method = RequestMethod.PUT)
	@ApiOperation(value = "Update last location of survivor", response = Survivor.class,
			notes = "Pass last location (latitude and longitude) of survivor")
	@ApiResponses(value = {
	        @ApiResponse(code = 404, message = "Suvivor not found"),
	        @ApiResponse(code = 200, message = "Successfully update last location")
		}
	)
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
