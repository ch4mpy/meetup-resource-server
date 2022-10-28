package dev.tahiti.meetup.jpa;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tahiti.meetup.domain.Order;
import dev.tahiti.meetup.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByTableLabelLikeIgnoreCaseAndStatusIn(String tableLabel, Collection<OrderStatus> status);
	
}
