package dev.tahiti.meetup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Drink {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false, unique = true)
	private String label;

	public Drink(String label) {
		super();
		this.label = label;
	}
	
}
