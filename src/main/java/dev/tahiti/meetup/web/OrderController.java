package dev.tahiti.meetup.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.tahiti.meetup.domain.DrinkOrder;
import dev.tahiti.meetup.domain.Order;
import dev.tahiti.meetup.domain.OrderStatus;
import dev.tahiti.meetup.jpa.DrinkRepository;
import dev.tahiti.meetup.jpa.OrderRepository;
import dev.tahiti.meetup.jpa.TableRepository;
import dev.tahiti.meetup.web.dto.OrderCreationDto;
import dev.tahiti.meetup.web.dto.OrderLineDto;
import dev.tahiti.meetup.web.dto.OrderResponseDto;
import dev.tahiti.meetup.web.dto.OrderStatusUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	private final DrinkRepository drinkRepo;
	private final OrderRepository orderRepo;
	private final TableRepository tableRepo;

	@GetMapping
	@Transactional(readOnly = true)
	List<OrderResponseDto> getTableOrders(@RequestParam(required = true, name = "table") String tableName,
			@RequestParam(required = false, defaultValue = "PLACED,SERVED") List<OrderStatus> statuses) {
		final var orders = orderRepo.findByTableLabelLikeIgnoreCaseAndStatusIn(tableName, statuses);
		return orders
				.stream().map(OrderController::map)
				.toList();
	}

	@GetMapping("/{id}")
	@Transactional(readOnly = true)
	OrderResponseDto getOrderById(@PathVariable(name = "orderId") Long orderId) {
		return orderRepo.findById(orderId).map(OrderController::map).orElseThrow();
	}

	@PostMapping
	@Transactional(readOnly = false)
	ResponseEntity<?> placeOrder(@RequestBody @Valid OrderCreationDto dto) throws URISyntaxException {
		final var table = tableRepo.findByLabel(dto.getTable()).orElse(null);
		var order = new Order(table);
		order.setDrinks(dto.getLines().stream()
				.map(l -> drinkRepo.findByLabel(l.getDrink()).map(d -> new DrinkOrder(l.getQuantity(), d)))
				.filter(Optional::isPresent).map(Optional::get).toList());
		order.setStatus(OrderStatus.PLACED);
		order = orderRepo.save(order);
		return ResponseEntity.accepted().location(new URI("/orders/%d".formatted(order.getId()))).build();
	}
	
	@PutMapping("/{id}/status")
	@Transactional(readOnly = false)
	ResponseEntity<?> updateOrder(@PathVariable(name = "orderId") Long orderId, @RequestBody OrderStatusUpdateDto dto) {
		final var order = orderRepo.findById(orderId).orElseThrow();
		order.setStatus(dto.getNewStatus());
		orderRepo.save(order);
		return ResponseEntity.accepted().build();
	}

	static OrderResponseDto map(Order o) {
		return new OrderResponseDto(o.getId(), o.getTable().getLabel(), o.getStatus().name(),
				o.getDrinks().stream().map(d -> new OrderLineDto(d.getDrink().getLabel(), d.getQuantity())).toList());
	}
}
