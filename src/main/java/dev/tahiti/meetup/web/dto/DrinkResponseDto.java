package dev.tahiti.meetup.web.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrinkResponseDto implements Serializable {
	private static final long serialVersionUID = -3146855358370943563L;

	@NotNull
	@NotEmpty
	private String label;
	
}
