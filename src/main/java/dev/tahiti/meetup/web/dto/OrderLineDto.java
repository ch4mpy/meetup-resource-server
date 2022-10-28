package dev.tahiti.meetup.web.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDto implements Serializable {
	private static final long serialVersionUID = -3146855358370943563L;
	
	@NotNull
	@NotEmpty
	private String drink;
	
	@NotNull
	@Min(0)
	private Float quantity;
	
}
