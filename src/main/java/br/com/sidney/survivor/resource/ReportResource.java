package br.com.sidney.survivor.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sidney.survivor.model.ItemEnum;
import br.com.sidney.survivor.model.ReportAverageResource;
import br.com.sidney.survivor.model.ReportInfectedPercentage;
import br.com.sidney.survivor.model.ReportLostPoints;
import br.com.sidney.survivor.model.ReportNonInfectedPercentage;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.repository.SurvivorsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/reports")
@Api(value = "Report", tags = "Reports")
public class ReportResource {

	public static final Logger logger = LoggerFactory.getLogger(SurvivorResource.class);

	@Autowired
	private SurvivorsRepository survivors;
	
	public ReportResource(SurvivorsRepository survivors) {
		super();
		this.survivors = survivors;
	}
	
	
	@RequestMapping(value = "/infected", method = RequestMethod.GET)
	@ApiOperation(value = "Percentage of infected survivors", response = ReportInfectedPercentage.class)
	public ResponseEntity<?> infectedPercentage() {

		List<Survivor> survivorsList = survivors.findAll();
		int all_number = survivorsList.size();

		List<Survivor> infecteds = survivorsList.stream().filter(survivor -> survivor.isInfected())
				.collect(Collectors.toList());

		int infected_number = infecteds.size();

		double percent_infected = (double) (infected_number * 100) / all_number;

		ReportInfectedPercentage infectedPercentage = new ReportInfectedPercentage(percent_infected);

		return new ResponseEntity<>(infectedPercentage, HttpStatus.OK);
	}

	@RequestMapping(value = "/non_infected", method = RequestMethod.GET)
	@ApiOperation(value = "Percentage of non-infected survivors", response = ReportNonInfectedPercentage.class)
	public ResponseEntity<?> nonInfectedPercentage() {

		List<Survivor> survivorsList = survivors.findAll();
		int all_number = survivorsList.size();

		List<Survivor> non_infecteds = survivorsList.stream().filter(survivor -> !survivor.isInfected())
				.collect(Collectors.toList());

		double non_infecteds_number = non_infecteds.size();

		double percent_non_infected = (double) (non_infecteds_number * 100) / all_number;

		ReportNonInfectedPercentage infectedPercentage = new ReportNonInfectedPercentage(percent_non_infected);

		return new ResponseEntity<>(infectedPercentage, HttpStatus.OK);
	}

	@RequestMapping(value = "/resource_average", method = RequestMethod.GET)
	@ApiOperation(value = "Average amount of each kind of resource by survivor (e.g. 5 waters per survivor)", 
	response = ReportAverageResource.class, notes = "This is important to we know hos long we gonna least")
	public ResponseEntity<?> resorceAverage() {

		List<Survivor> survivorsList = survivors.findAll();

		double water = survivorsList.stream().mapToInt(s -> s.getInventory().getItems().get(ItemEnum.Water)).sum();

		double food = survivorsList.stream().mapToInt(s -> s.getInventory().getItems().get(ItemEnum.Food)).sum();
		;

		double medication = survivorsList.stream().mapToInt(s -> s.getInventory().getItems().get(ItemEnum.Medication))
				.sum();
		;

		double ammunition = survivorsList.stream().mapToInt(s -> s.getInventory().getItems().get(ItemEnum.Ammunition))
				.sum();
		;

		double waterAverage = (double) (water / survivorsList.size());
		double foodAverage = (double) (food / survivorsList.size());
		double medicationAverage = (double) (medication / survivorsList.size());
		double ammunitionAverage = (double) (ammunition / survivorsList.size());

		ReportAverageResource reportAverageResource = new ReportAverageResource(waterAverage, foodAverage,
				medicationAverage, ammunitionAverage);

		return new ResponseEntity<>(reportAverageResource, HttpStatus.OK);
	}

	@RequestMapping(value = "/lost_points", method = RequestMethod.GET)
	@ApiOperation(value = "Points lost because of infected survivor", response = ReportLostPoints.class)
	public ResponseEntity<?> lostPoints() {
		List<Survivor> survivorsList = survivors.findAll();

		int lost_points = survivorsList.stream().filter(s -> s.isInfected()).mapToInt(s -> s.getPoints()).sum();

		ReportLostPoints reportLostPoints = new ReportLostPoints(lost_points);

		return new ResponseEntity<>(reportLostPoints, HttpStatus.OK);
	}

}
