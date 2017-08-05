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

@Entity
public class Inventory {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@ElementCollection
	@MapKeyColumn(name="Item")
    @Column(name="Quantity")
	@CollectionTable(name="inventory_items", joinColumns=@JoinColumn(name="inventory_id"))
	private Map<ItemEnum, Integer> items;
	
	public Inventory() {
		super();
		items = new HashMap<ItemEnum, Integer>();
	}
	
	public Inventory(HashMap<ItemEnum, Integer> items) {
		super();
		this.items = items;
	}
	
	public void addItem(ItemEnum item, int quantity){
		if (this.items.containsKey(item)){
			Integer new_value = this.items.get(item) + quantity;
			this.items.put(item, new_value); 
		} else {
			this.items.put(item, quantity);
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Map<ItemEnum, Integer> getItems() {
		return items;
	}

	public void setItems(HashMap<ItemEnum, Integer> itens) {
		this.items = itens;
	}
	
	public boolean hasItensForTrade(Map<ItemEnum, Integer> itemsTrade){
		
		for (ItemEnum item : itemsTrade.keySet()) {
			if (!items.containsKey(item)){
				return false;
			}
			if (items.get(item) < itemsTrade.get(item)){
				return false;
			}
		}
		return true;
	}
	
	public void tradeSell(Map<ItemEnum, Integer> itemsTrade){
		for (ItemEnum item : items.keySet()) {
			int quantity = items.get(item) - itemsTrade.get(item);
			items.put(item, quantity);
		}
	}
	
	public void tradeBuy(Map<ItemEnum, Integer> itemsTrade){
		for (ItemEnum item : items.keySet()) {
			int quantity = items.get(item) + itemsTrade.get(item);
			items.put(item, quantity);
		}
	}
	
	@Override
	public String toString() {
		return "Inventory [id=" + id + ", items=" + items + "]";
	}
	
	
}

	