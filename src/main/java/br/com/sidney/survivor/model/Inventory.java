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
	
	public Map<ItemEnum, Integer> getItens() {
		return items;
	}

	public void setItens(HashMap<ItemEnum, Integer> itens) {
		this.items = itens;
	}

	@Override
	public String toString() {
		return "Inventory [id=" + id + ", items=" + items + "]";
	}
	
	
}

	