package br.com.sidney.survivor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sidney.survivor.model.Survivor;

public interface Survivors extends JpaRepository<Survivor, Long> {

}
