package dev.tahiti.meetup.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tahiti.meetup.domain.Drink;

public interface DrinkRepository extends JpaRepository<Drink, Long> {

	Optional<Drink> findByLabel(String label);
	List<Drink> findByLabelLikeIgnoreCase(String label);
	
}
