package dev.tahiti.meetup.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@jakarta.persistence.Table(name = "bar_order")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue
	Long id;
	
	@ManyToOne(optional = true)
	private Table table;
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	private List<DrinkOrder> drinks = new ArrayList<>();
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PLACED;

	public Order(Table table) {
		super();
		this.table = table;
	}
	
}
