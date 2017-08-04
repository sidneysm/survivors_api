package br.com.sidney.survivor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sidney.survivor.model.Inventory;

public interface Inventories extends JpaRepository<Inventory, Long> {

}
