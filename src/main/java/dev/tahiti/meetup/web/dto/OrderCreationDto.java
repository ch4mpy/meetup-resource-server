package dev.tahiti.meetup.web.dto;

import java.util.List;

import org.springframework.lang.Nullable;

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
public class OrderCreationDto {

	@Nullable
	private String table;

	@NotNull
	@NotEmpty
	private List<OrderLineDto> lines;
	
}
