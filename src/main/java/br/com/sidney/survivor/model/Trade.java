package br.com.sidney.survivor.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Trade {
		
	@Id
	@GeneratedValue
	@ApiModelProperty(notes = "The database generated product ID")
	private Long id;
	
	@OneToOne
	@Cascade({CascadeType.MERGE})
	@ApiModelProperty(notes = "The survivor who selling")
	private Survivor seller;
	
	@OneToOne
	@Cascade({CascadeType.MERGE})
	@ApiModelProperty(notes = "The survivor who buy")
	private Survivor buyer;
	
	@ElementCollection
	@MapKeyColumn(name="Items")
    @Column(name="Quantity")
	@CollectionTable(name="trade_items", joinColumns=@JoinColumn(name="trade_id"))
	@ApiModelProperty(notes = "The traded itens")
	private Map<ItemEnum, Integer> items;
	
	public Trade() {
		items = new HashMap<ItemEnum, Integer>();
	}

	public Trade(Long id, Survivor seller, Survivor buyer, Map<ItemEnum, Integer> items) {
		this.id = id;
		this.seller = seller;
		this.buyer = buyer;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Survivor getSeller() {
		return seller;
	}

	public void setSeller(Survivor seller) {
		this.seller = seller;
	}

	public Survivor getBuyer() {
		return buyer;
	}

	public void setBuyer(Survivor buyer) {
		this.buyer = buyer;
	}

	public Map<ItemEnum, Integer> getItems() {
		return items;
	}

	public void setItems(Map<ItemEnum, Integer> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Trade [id=" + id + ", seller=" + seller + ", buyer=" + buyer + ", items=" + items + "]";
	}
	
	// Runs over trade items to calculate total prize
	public int totalPriceItems(){
		
		int total = 0;
		for (ItemEnum item : items.keySet()) {
			String i = item.name();
			switch (i) {
			case "Water":
				total += 4 * items.get(item);
				break;
			case "Food":
				total += 3 * items.get(item);
				break;
			case "Medication":
				total += 2 * items.get(item);
				break;
			case "Ammunition":
				total += items.get(item);
				break;
			default:
				break;
			}
		}
		return total;
	}
	
	public boolean makeTrade() {
		// Check is the conditions for make trade is ok.
		if (buyer.getPoints() >= totalPriceItems() && 
				seller.getInventory().hasItensForTrade(items) &&
					!buyer.isInfected() && !seller.isInfected()){
			
			// Set new buyer and seller points
			buyer.setPoints(buyer.getPoints() - totalPriceItems());
			seller.setPoints(seller.getPoints() + totalPriceItems());
			
			// Update seller and buyer's inventory
			seller.getInventory().tradeSell(items);
			buyer.getInventory().tradeBuy(items);
			return true;
		}
		return false;
	}
	
	
	
}
