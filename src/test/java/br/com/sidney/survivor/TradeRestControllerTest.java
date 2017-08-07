package br.com.sidney.survivor;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import br.com.sidney.survivor.model.Inventory;
import br.com.sidney.survivor.model.ItemEnum;
import br.com.sidney.survivor.model.Location;
import br.com.sidney.survivor.model.Survivor;
import br.com.sidney.survivor.model.Trade;
import br.com.sidney.survivor.repository.SurvivorsRepository;
import br.com.sidney.survivor.repository.TradesRepository;
import br.com.sidney.survivor.resource.SurvivorResource;
import br.com.sidney.survivor.resource.TradeResource;
import util.TestUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuvivorsApplication.class)
@WebAppConfiguration
public class TradeRestControllerTest {

	private MockMvc mockMvc;
	private MockMvc mockMvcSurv;
	
	@Autowired
	private SurvivorsRepository survivorsRepository;

	@Autowired
	private TradesRepository tradesRepository;
	
	
	private Survivor survivor = new Survivor(); 
	private Survivor survivor2 = new Survivor();
	private Inventory inventory1 = new Inventory();
	private Inventory inventory2 = new Inventory();
	private Inventory inventory3 = new Inventory();
	private Trade trade = new Trade();

	@Before
	public void setup() {
		tradesRepository = mock(TradesRepository.class);
		survivorsRepository = mock(SurvivorsRepository.class);
		reset(tradesRepository);
		reset(survivorsRepository);
		this.mockMvc = standaloneSetup(new TradeResource(tradesRepository, survivorsRepository)).build();
		
		this.mockMvcSurv = standaloneSetup(new SurvivorResource(survivorsRepository)).build();
		
//		Create the survivors for trade.
		Inventory inventory1 = new Inventory();
		inventory1.setId(1l);
		inventory1.getItems().put(ItemEnum.Food, 4);
		inventory1.getItems().put(ItemEnum.Medication, 4);
		inventory1.getItems().put(ItemEnum.Ammunition, 4);
		inventory1.getItems().put(ItemEnum.Water, 4);
		
		inventory2.setId(2l);
		inventory2.getItems().put(ItemEnum.Food, 4);
		inventory2.getItems().put(ItemEnum.Medication, 4);
		inventory2.getItems().put(ItemEnum.Ammunition, 4);
		inventory2.getItems().put(ItemEnum.Water, 4);
		
		inventory3.setId(3l);
		inventory3.getItems().put(ItemEnum.Food, 4);
		inventory3.getItems().put(ItemEnum.Medication, 4);
		inventory3.getItems().put(ItemEnum.Ammunition, 4);
		inventory3.getItems().put(ItemEnum.Water, 4);
		
//		Survivor survivor = new Survivor();

		survivor.setInventory(inventory1);

		Location location = new Location();
		location.setId(1l);
		location.setLatitude(5.5);
		location.setLongitude(6.1);

		survivor.setLastLocation(location);

		survivor.setId(1l);
		survivor.setPoints(1000);
		survivor.setAge(31);
		survivor.setName("Sidney");

//		Survivor survivor2 = new Survivor();
		survivor2.setId(2l);
		survivor2.setPoints(100);
		survivor2.setAge(31);
		survivor2.setName("Soares");
		survivor2.setLastLocation(location);
		survivor2.setInventory(inventory2);

		trade.setId(1l);
		trade.setBuyer(survivor);
		trade.setSeller(survivor2);
		trade.setItems(inventory3.getItems());
		
	}
	
	@Test
	public void testTrade() throws Exception {
//		
		
		when(survivorsRepository.findOne(1l)).thenReturn(survivor);
		when(survivorsRepository.findOne(2l)).thenReturn(survivor2);
		when(tradesRepository.save(any(Trade.class))).thenReturn(trade);
		
		this.mockMvc
			.perform(post("/trade").accept(TestUtil.APPLICATION_JSON_UTF8).param("buyer", "1").param("seller", "2")
					.content(TestUtil.convertObjectToJsonBytes(inventory3))
					.contentType(TestUtil.APPLICATION_JSON_UTF8)).andDo(print())
			.andExpect(status().isCreated()).andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.seller.id", is(2)))
			.andExpect(jsonPath("$.seller.name", is("Soares")))
			.andExpect(jsonPath("$.items.Food", is(4)))
			.andExpect(jsonPath("$.items.Water", is(4)))
			.andExpect(jsonPath("$.items.Ammunition", is(4)))
			.andExpect(jsonPath("$.items.Medication", is(4)))
			.andExpect(jsonPath("$.buyer.name", is("Sidney")))
			.andExpect(jsonPath("$.buyer.inventory.items.Food", is(8)))
			.andExpect(jsonPath("$.buyer.inventory.items.Water", is(8)))
			.andExpect(jsonPath("$.buyer.inventory.items.Ammunition", is(8)))
			.andExpect(jsonPath("$.buyer.inventory.items.Medication", is(8)))
			.andExpect(jsonPath("$.buyer.points", is(960)))
			.andExpect(jsonPath("$.seller.inventory.items.Food", is(0)))
			.andExpect(jsonPath("$.seller.inventory.items.Water", is(0)))
			.andExpect(jsonPath("$.seller.inventory.items.Ammunition", is(0)))
			.andExpect(jsonPath("$.seller.inventory.items.Medication", is(0)))
			.andExpect(jsonPath("$.seller.points", is(140)));
		
	}
	
	@Test
	public void testIfBuyerIsinfected() throws Exception {
//		Create the survivors for trade.
		
		survivor.setInfected(true);
		
		when(survivorsRepository.findOne(1l)).thenReturn(survivor);
		when(survivorsRepository.findOne(2l)).thenReturn(survivor2);
		when(tradesRepository.save(any(Trade.class))).thenReturn(trade);
		
		this.mockMvc
				.perform(post("/trade").accept(TestUtil.APPLICATION_JSON_UTF8).param("buyer", "1").param("seller", "2")
						.content(TestUtil.convertObjectToJsonBytes(inventory3)).contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testIfSellerIsinfected() throws Exception {
		survivor2.setInfected(true);
		
		when(survivorsRepository.findOne(1l)).thenReturn(survivor);
		when(survivorsRepository.findOne(2l)).thenReturn(survivor2);
		when(tradesRepository.save(any(Trade.class))).thenReturn(trade);
		
		this.mockMvc
				.perform(post("/trade").accept(TestUtil.APPLICATION_JSON_UTF8).param("buyer", "1").param("seller", "2")
						.content(TestUtil.convertObjectToJsonBytes(inventory3)).contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testIfSellerHasItems() throws Exception {
		inventory3.getItems().put(ItemEnum.Food, 5);
		inventory3.getItems().put(ItemEnum.Medication, 5);
		inventory3.getItems().put(ItemEnum.Ammunition, 5);
		inventory3.getItems().put(ItemEnum.Water, 5);
		
		when(survivorsRepository.findOne(1l)).thenReturn(survivor);
		when(survivorsRepository.findOne(2l)).thenReturn(survivor2);
		when(tradesRepository.save(any(Trade.class))).thenReturn(trade);
		
		this.mockMvc
				.perform(post("/trade").accept(TestUtil.APPLICATION_JSON_UTF8).param("buyer", "1").param("seller", "2")
						.content(TestUtil.convertObjectToJsonBytes(inventory3)).contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testIfBuyerHasPoints() throws Exception {
		survivor.setPoints(0);
		
		when(survivorsRepository.findOne(1l)).thenReturn(survivor);
		when(survivorsRepository.findOne(2l)).thenReturn(survivor2);
		when(tradesRepository.save(any(Trade.class))).thenReturn(trade);
		
		this.mockMvc
				.perform(post("/trade").accept(TestUtil.APPLICATION_JSON_UTF8).param("buyer", "1").param("seller", "2")
						.content(TestUtil.convertObjectToJsonBytes(inventory3)).contentType(TestUtil.APPLICATION_JSON_UTF8))
			.andExpect(status().isBadRequest());
	}
	
}
