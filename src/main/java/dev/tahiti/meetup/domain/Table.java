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

@jakarta.persistence.Table(name = "BAR_TABLE")
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
public class Table {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false, unique = true)
	private String label;

	public Table(String label) {
		super();
		this.label = label;
	}
	
}
