package br.com.sidney.survivor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sidney.survivor.model.Trade;

public interface Trades extends JpaRepository<Trade, Long> {

}
