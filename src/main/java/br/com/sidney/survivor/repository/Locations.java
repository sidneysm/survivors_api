package br.com.sidney.survivor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sidney.survivor.model.Location;

public interface Locations extends JpaRepository<Location, Long> {

}
