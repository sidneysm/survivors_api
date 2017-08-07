package br.com.sidney.survivor;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import br.com.sidney.survivor.model.Inventory;
import br.com.sidney.survivor.model.ItemEnum;
import br.com.sidney.survivor.model.Location;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.repository.SurvivorsRepository;
import br.com.sidney.survivor.resource.ReportResource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuvivorsApplication.class)
@WebAppConfiguration
public class ReportRestControllerTest {
	
	private MockMvc mockMvc;

	@Autowired
	private SurvivorsRepository survivorsRepository;
	
	private Survivor survivor = new Survivor();
	private Survivor survivor2 = new Survivor();
	private Survivor survivor3 = new Survivor();
	private Inventory inventory1 = new Inventory();
	private Inventory inventory2 = new Inventory();
	private Inventory inventory3 = new Inventory();
	
	@Before
	public void setup() {
		survivorsRepository = mock(SurvivorsRepository.class);
		reset(survivorsRepository);
		this.mockMvc = standaloneSetup(new ReportResource(survivorsRepository)).build();
		
		// Creating objects for tests		
		
		inventory1.setId(1l);
		inventory1.getItems().put(ItemEnum.Food, 0);
		inventory1.getItems().put(ItemEnum.Medication, 2);
		inventory1.getItems().put(ItemEnum.Ammunition, 5);
		inventory1.getItems().put(ItemEnum.Water, 8);
		
		inventory2.setId(2l);
		inventory2.getItems().put(ItemEnum.Food, 1);
		inventory2.getItems().put(ItemEnum.Medication, 5);
		inventory2.getItems().put(ItemEnum.Ammunition, 3);
		inventory2.getItems().put(ItemEnum.Water, 6);
		
		inventory3.setId(3l);
		inventory3.getItems().put(ItemEnum.Food, 2);
		inventory3.getItems().put(ItemEnum.Medication, 6);
		inventory3.getItems().put(ItemEnum.Ammunition, 4);
		inventory3.getItems().put(ItemEnum.Water, 4);

		survivor.setInventory(inventory1);

		Location location = new Location();
		location.setId(1l);
		location.setLatitude(5.5);
		location.setLongitude(6.1);

		survivor.setLastLocation(location);

		survivor.setId(1l);
		survivor.setPoints(100);
		survivor.setAge(31);
		survivor.setName("Sidney");

		
		survivor2.setId(2l);
		survivor2.setPoints(100);
		survivor2.setAge(31);
		survivor2.setName("Soares");
		survivor2.setLastLocation(location);
		survivor2.setInventory(inventory2);
		
		survivor3.setId(3l);
		survivor3.setPoints(100);
		survivor3.setAge(31);
		survivor3.setName("Marcelino");
		survivor3.setLastLocation(location);
		survivor3.setInventory(inventory3);
		survivor3.setInfected(true);
	}
	
	// Test if endpoint "/reports/infected" works
	@Test
	public void testReportInfecteds() throws Exception {

		when(survivorsRepository.findAll()).thenReturn(Arrays.asList(survivor, survivor2, survivor3));

		this.mockMvc.perform(get("/reports/infected").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.description", is("Percentage of infected survivors")))
				.andExpect(jsonPath("$.percentage", is(33.333333333333336)));
				

	}
	
	// Test if endpoint "/reports/non_infected" works
	@Test
	public void testReportNonInfecteds() throws Exception {

		when(survivorsRepository.findAll()).thenReturn(Arrays.asList(survivor, survivor2, survivor3));

		this.mockMvc.perform(get("/reports/non_infected").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.description", is("Percentage of non infected survivors")))
				.andExpect(jsonPath("$.percentage", is(66.666666666666667)));
	}
	
	// Test if endpoint "/reports/lost_points" works
	@Test
	public void testReportLostPoints() throws Exception {

		when(survivorsRepository.findAll()).thenReturn(Arrays.asList(survivor, survivor2, survivor3));

		this.mockMvc.perform(get("/reports/lost_points").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.description", is("Points lost because of infected survivor")))
				.andExpect(jsonPath("$.lostPoints", is(100)));
	}
	
	// Test if endpoint "/reports/resource_average" works
	@Test
	public void testReportAvaregeResouces() throws Exception {

		when(survivorsRepository.findAll()).thenReturn(Arrays.asList(survivor, survivor2, survivor3));

		this.mockMvc.perform(get("/reports/resource_average").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.description", is("Average amount of each kind of resource by survivor")))
				.andExpect(jsonPath("$.waterAverage", is(6.0)))
				.andExpect(jsonPath("$.foodAverage", is(1.0)))
				.andExpect(jsonPath("$.ammunitionAverage", is(4.0)))
				.andExpect(jsonPath("$.medicationAverage", is(4.333333333333333)));
	}
}
