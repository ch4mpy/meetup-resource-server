package dev.tahiti.meetup.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
public class DrinkOrder {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false)
	private Float quantity;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Drink drink;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	private Order order;

	public DrinkOrder(Order order, float quantity, Drink drink) {
		super();
		this.order = order;
		this.quantity = quantity;
		this.drink = drink;
	}
	
}
