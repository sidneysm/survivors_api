package br.com.sidney.survivor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import br.com.sidney.survivor.SuvivorsApplication;
import br.com.sidney.survivor.model.Inventory;
import br.com.sidney.survivor.model.ItemEnum;
import br.com.sidney.survivor.model.Location;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.repository.SurvivorsRepository;
import br.com.sidney.survivor.resource.SurvivorResource;
import util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuvivorsApplication.class)
@WebAppConfiguration
public class SurvivorRestControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private SurvivorsRepository survivorsRepository;

	@Before
	public void setup() {
		survivorsRepository = mock(SurvivorsRepository.class);
		reset(survivorsRepository);
		this.mockMvc = standaloneSetup(new SurvivorResource(survivorsRepository)).build();
	}

	@Test
	public void testListSurvivors() throws Exception {
		Inventory inventory = new Inventory();

		Survivor survivor = new Survivor();

		inventory.getItems().put(ItemEnum.Food, 4);
		inventory.getItems().put(ItemEnum.Medication, 5);
		inventory.getItems().put(ItemEnum.Ammunition, 7);
		inventory.getItems().put(ItemEnum.Water, 4);

		survivor.setInventory(inventory);

		Location location = new Location();
		location.setId(1l);
		location.setLatitude(5.5);
		location.setLongitude(6.1);

		survivor.setLastLocation(location);

		survivor.setId(1l);
		survivor.setPoints(100);
		survivor.setAge(31);
		survivor.setName("Sidney");

		Survivor survivor2 = new Survivor();
		survivor2.setId(2l);
		survivor2.setPoints(100);
		survivor2.setAge(31);
		survivor2.setName("Soares");
		survivor2.setLastLocation(location);
		survivor2.setInventory(inventory);

		when(survivorsRepository.findAll()).thenReturn(Arrays.asList(survivor, survivor2));

		this.mockMvc.perform(get("/survivors/list").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("Sidney"))).andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Soares")));

	}

	@Test
	public void testAddSurvivor() throws Exception {

		Inventory inventory = new Inventory();

		Survivor survivor = new Survivor();

		inventory.getItems().put(ItemEnum.Food, 4);
		inventory.getItems().put(ItemEnum.Medication, 5);
		inventory.getItems().put(ItemEnum.Ammunition, 7);
		inventory.getItems().put(ItemEnum.Water, 4);

		survivor.setInventory(inventory);

		Location location = new Location();
		location.setId(1l);
		location.setLatitude(5.5);
		location.setLongitude(6.1);

		survivor.setLastLocation(location);

		survivor.setId(1l);
		survivor.setPoints(100);
		survivor.setAge(31);
		survivor.setName("Sidney");

		Survivor survivor2 = survivor;
		survivor2.setId(1l);
		survivor2.setPoints(100);
		survivor2.setAge(31);
		survivor2.setName("Sidney");
		survivor2.setLastLocation(location);
		survivor2.setInventory(inventory);

		when(survivorsRepository.save(any(Survivor.class))).thenReturn(survivor2);

		this.mockMvc
				.perform(post("/survivors/add").accept(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(survivor))
						.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Sidney")))
				.andExpect(jsonPath("$.lastLocation.latitude", is(5.5)))
				.andExpect(jsonPath("$.lastLocation.longitude", is(6.1)))
				.andExpect(jsonPath("$.inventory.items.Food", is(4)))
				.andExpect(jsonPath("$.inventory.items.Water", is(4)))
				.andExpect(jsonPath("$.inventory.items.Ammunition", is(7)))
				.andExpect(jsonPath("$.inventory.items.Medication", is(5))).andExpect(jsonPath("$.points", is(100)))
				.andExpect(jsonPath("$.infected", is(false)));
	}

	@Test
	public void testInfected() throws Exception {
		Inventory inventory = new Inventory();

		Survivor survivor = new Survivor();

		inventory.getItems().put(ItemEnum.Food, 4);
		inventory.getItems().put(ItemEnum.Medication, 5);
		inventory.getItems().put(ItemEnum.Ammunition, 7);
		inventory.getItems().put(ItemEnum.Water, 4);

		survivor.setInventory(inventory);

		Location location = new Location();
		location.setId(1L);
		location.setLatitude(5.5);
		location.setLongitude(6.1);

		survivor.setLastLocation(location);

		survivor.setId(1L);
		survivor.setPoints(100);
		survivor.setAge(31);
		survivor.setName("Sidney");

		when(survivorsRepository.findOne(1l)).thenReturn(survivor);

		this.mockMvc.perform(put("/survivors/{id}/infected", 1l).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(jsonPath("$.infected", is(true)));

		// Not found survivor
		this.mockMvc.perform(put("/survivors/{id}/infected", 2l).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testLastLocationUpdate() throws Exception {
		Inventory inventory = new Inventory();

		Survivor survivor = new Survivor();

		survivor.setInventory(inventory);

		Location location = new Location();
		location.setId(1L);
		location.setLatitude(5.5);
		location.setLongitude(6.1);

		survivor.setLastLocation(location);

		survivor.setId(1L);
		survivor.setPoints(100);
		survivor.setAge(31);
		survivor.setName("Sidney");

		Location location2 = new Location();
		location2.setId(2L);
		location2.setLatitude(3);
		location2.setLongitude(5);
		when(survivorsRepository.findOne(1l)).thenReturn(survivor);

		this.mockMvc.perform(put("/survivors/{id}/location", 1l).accept(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(location2)).contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastLocation.latitude", is(3.0)))
				.andExpect(jsonPath("$.lastLocation.longitude", is(5.0)));
		
		// No content
		this.mockMvc.perform(put("/survivors/{id}/location", 2l).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
		
		// Not found survivor
		this.mockMvc.perform(put("/survivors/{id}/location", 2l).accept(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(location2)).contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}
}
