package dev.tahiti.meetup.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tahiti.meetup.domain.Table;
import dev.tahiti.meetup.jpa.TableRepository;
import dev.tahiti.meetup.web.dto.TableResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {
	private final TableRepository tableRepo;

	@GetMapping
	public List<TableResponseDto> getAllTables() {
		return tableRepo.findAll().stream().map(Table::getLabel).map(TableResponseDto::new).toList();
	}
	
}
