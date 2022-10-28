package dev.tahiti.meetup.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tahiti.meetup.domain.Table;

public interface TableRepository extends JpaRepository<Table, Long> {

	Optional<Table> findByLabel(String label);
	List<Table> findByLabelLikeIgnoreCase(String label);
	
}
