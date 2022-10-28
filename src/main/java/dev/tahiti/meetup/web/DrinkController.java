package dev.tahiti.meetup.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tahiti.meetup.domain.Drink;
import dev.tahiti.meetup.jpa.DrinkRepository;
import dev.tahiti.meetup.web.dto.DrinkResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/drinks")
@RequiredArgsConstructor
public class DrinkController {
	private final DrinkRepository drinkRepo;

	@GetMapping
	public List<DrinkResponseDto> getAllTables() {
		return drinkRepo.findAll().stream().map(Drink::getLabel).map(DrinkResponseDto::new).toList();
	}
	
}
