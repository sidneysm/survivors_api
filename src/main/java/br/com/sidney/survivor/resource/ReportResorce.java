package br.com.sidney.survivor.resource;


import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sidney.survivor.model.ReportInfectedPercentage;
import br.com.sidney.survivor.model.ReportNonInfectedPercentage;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.repository.Survivors;

@RestController
@RequestMapping(value = "reports")
public class ReportResorce {
	
	public static final Logger logger = LoggerFactory.getLogger(SurvivorResource.class);
	
	@Autowired
	private Survivors survivors;
	
	@RequestMapping(value = "/infected")
	public ResponseEntity<?> infectedPercentage(){
		
		List<Survivor> survivorsList = survivors.findAll();
		int all_number = survivorsList.size();
		
		List<Survivor> infecteds = survivorsList.stream()
				.filter(survivor -> survivor.isInfected())
				.collect(Collectors.toList());
		
		int infected_number = infecteds.size();
		
		double percent_infected = (double)(infected_number*100)/ all_number;		
		
		ReportInfectedPercentage infectedPercentage = new ReportInfectedPercentage(percent_infected);
		
		return new ResponseEntity<>(infectedPercentage, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/non_infected")
	public ResponseEntity<?> nonInfectedPercentage(){
		
		List<Survivor> survivorsList = survivors.findAll();
		int all_number = survivorsList.size();
		
		List<Survivor> non_infecteds = survivorsList.stream()
				.filter(survivor -> !survivor.isInfected())
				.collect(Collectors.toList());
		
		int non_infecteds_number = non_infecteds.size();
		
		double percent_non_infected = (double)(non_infecteds_number*100)/ all_number;		
		
		ReportNonInfectedPercentage infectedPercentage = new ReportNonInfectedPercentage(percent_non_infected);
		
		return new ResponseEntity<>(infectedPercentage, HttpStatus.OK);
	}
	
}
