package br.com.sidney.survivor.model;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sidney.survivor.resource.SurvivorResource;

@Entity
public class Trade {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	@Cascade({CascadeType.MERGE})
	private Survivor seller;
	
	@OneToOne
	@Cascade({CascadeType.MERGE})
	private Survivor buyer;
	
	@ElementCollection
	@MapKeyColumn(name="Items")
    @Column(name="Quantity")
	@CollectionTable(name="trade_items", joinColumns=@JoinColumn(name="trade_id"))
	private Map<ItemEnum, Integer> items;
	
	public Trade() {
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
		if (buyer.getPoints() >= totalPriceItems() && 
				seller.getInventory().hasItensForTrade(items) &&
					!buyer.isInfected() && !seller.isInfected()){
			
			buyer.setPoints(buyer.getPoints() - totalPriceItems());
			seller.setPoints(seller.getPoints() + totalPriceItems());
			
			seller.getInventory().tradeSell(items);
			buyer.getInventory().tradeBuy(items);
			return true;
		}
		return false;
	}
	
	
	
}
