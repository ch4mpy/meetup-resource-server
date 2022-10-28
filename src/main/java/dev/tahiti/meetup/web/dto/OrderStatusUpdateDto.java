package dev.tahiti.meetup.web.dto;

import dev.tahiti.meetup.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateDto {
	@NotNull
	private OrderStatus newStatus;
}
