package dev.tahiti.meetup.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenId;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.c4_soft.springaddons.security.oauth2.test.webmvc.jwt.AutoConfigureAddonsWebSecurity;

import dev.tahiti.meetup.EnableSpringDataWebSupportTestConf;
import dev.tahiti.meetup.configuration.SecurityConfig;
import dev.tahiti.meetup.domain.Drink;
import dev.tahiti.meetup.domain.DrinkOrder;
import dev.tahiti.meetup.domain.Order;
import dev.tahiti.meetup.domain.OrderStatus;
import dev.tahiti.meetup.domain.Table;
import dev.tahiti.meetup.jpa.DrinkRepository;
import dev.tahiti.meetup.jpa.OrderRepository;
import dev.tahiti.meetup.jpa.TableRepository;
import dev.tahiti.meetup.web.dto.OrderCreationDto;
import dev.tahiti.meetup.web.dto.OrderLineDto;
import dev.tahiti.meetup.web.dto.OrderStatusUpdateDto;

@WebMvcTest(controllers = { OrderController.class })
@AutoConfigureAddonsWebSecurity
@Import({ SecurityConfig.class, EnableSpringDataWebSupportTestConf.class })
class OrderControllerTest {
	private static final String CH4MPY_SUBJECT = "ch4mpy";
	private static final String TABLE_ONE_LABEL = "One";

	@Autowired
	MockMvcSupport api;

	@MockBean
	DrinkRepository drinkRepo;
	@MockBean
	OrderRepository orderRepo;
	@MockBean
	TableRepository tableRepo;

	final Drink guinness = Drink.builder().id(1L).label("Guinness").build();
	final Drink mojito = Drink.builder().id(1L).label("Mojito").build();
	final Drink lemonade = Drink.builder().id(1L).label("Lemonade").build();
	
	final Table table1 = Table.builder().id(1L).label(TABLE_ONE_LABEL).build();

	final Order order42 = Order.builder().id(42L)
			.drinks(List.of(DrinkOrder.builder().id(1L).drink(guinness).quantity(0.5f).build(),
					DrinkOrder.builder().id(2L).drink(mojito).quantity(4f).build(),
					DrinkOrder.builder().id(3L).drink(lemonade).quantity(1f).build(),
					DrinkOrder.builder().id(4L).drink(guinness).quantity(0.33f).build()))
			.placedBy(CH4MPY_SUBJECT).build();
	

	@BeforeEach
	public void setUp() {
		when(orderRepo.findById(42L)).thenReturn(Optional.of(order42));
		when(tableRepo.findByLabel(TABLE_ONE_LABEL)).thenReturn(Optional.of(table1));
		when(drinkRepo.findByLabel("Guinness")).thenReturn(Optional.of(guinness));
	}

	/* getTableOrders access-control */
	@Test
	void whenAnonymousThenGetTableOrdersIsUnauthorized() throws Exception {
		api.get("/orders?table={tableName}", "One").andExpect(status().isUnauthorized());
	}

	@Test
	@OpenId("CLIENT")
	void whenJustClientThenGetTableOrdersIsForbidden() throws Exception {
		api.get("/orders?table={tableName}", "One").andExpect(status().isForbidden());
	}

	@Test
	@OpenId("WAITER")
	void whenWaiterThenGetTableOrdersIsOk() throws Exception {
		api.get("/orders?table={tableName}", "One").andExpect(status().isOk());
	}

	@Test
	@OpenId("BARMAN")
	void whenBarmanThenGetTableOrdersIsForbidden() throws Exception {
		api.get("/orders?table={tableName}", "One").andExpect(status().isOk());
	}

	@Test
	@OpenId("CASHIER")
	void whenCashierThenGetTableOrdersIsForbidden() throws Exception {
		api.get("/orders?table={tableName}", "One").andExpect(status().isOk());
	}

	/* getOrderById access-control */
	@Test
	void whenAnonymousThenGetOrderByIdIsUnauthorized() throws Exception {
		api.get("/orders/{orderId}", 42).andExpect(status().isUnauthorized());
	}

	@Test
	@OpenId()
	void whenClientDidNotPlacedOrderThenGetOrderByIdIsForbidden() throws Exception {
		api.get("/orders/{orderId}", 42).andExpect(status().isForbidden());
	}

	@Test
	@OpenId(claims = @OpenIdClaims(sub = CH4MPY_SUBJECT))
	void whenClientPlacedOrderThenGetOrderByIdIsOk() throws Exception {
		api.get("/orders/{orderId}", 42).andExpect(status().isOk());
	}

	@Test
	@OpenId("WAITER")
	void whenWaiterThenGetOrderByIdIsOk() throws Exception {
		api.get("/orders/{orderId}", 42).andExpect(status().isOk());
	}

	@Test
	@OpenId("BARMAN")
	void whenBarmanThenGetOrderByIdIsForbidden() throws Exception {
		api.get("/orders/{orderId}", 42).andExpect(status().isOk());
	}

	@Test
	@OpenId("CASHIER")
	void whenCashierThenGetOrderByIdIsForbidden() throws Exception {
		api.get("/orders/{orderId}", 42).andExpect(status().isOk());
	}

	/* placeOrder access-control */
	@Test
	void whenAnonymousThenPlaceOrderIsUnauthorized() throws Exception {
		api.post(new OrderCreationDto(null, List.of(new OrderLineDto("Guiness", 0.5f))), "/orders").andExpect(status().isUnauthorized());
	}
	
	@Test
	@OpenId
	void whenAuthenticatedThenPlaceOrderIsAccepted() throws Exception {
		when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> {
			final var order = (Order) invocation.getArguments()[0];
			order.setId(51L);
			return order;
		});
		api.post(new OrderCreationDto(null, List.of(new OrderLineDto("Guiness", 0.5f))), "/orders").andExpect(status().isAccepted());
	}

	/* updateOrderStatus access-control */
	@Test
	void whenAnonymousThenUpdateOrderStatusIsUnauthorized() throws Exception {
		api.put(new OrderStatusUpdateDto(OrderStatus.PAID), "/orders/{orderId}/status", 42).andExpect(status().isUnauthorized());
	}
	
	@Test
	@OpenId
	void whenClientThenUpdateOrderStatusIsForbidden() throws Exception {
		api.put(new OrderStatusUpdateDto(OrderStatus.PAID), "/orders/{orderId}/status", 42).andExpect(status().isForbidden());
	}
	
	@Test
	@OpenId("WAITER")
	void whenWaiterThenUpdateOrderStatusIsForbidden() throws Exception {
		api.put(new OrderStatusUpdateDto(OrderStatus.PAID), "/orders/{orderId}/status", 42).andExpect(status().isForbidden());
	}
	
	@Test
	@OpenId("BARMAN")
	void whenBarmanThenUpdateOrderStatusIsAccepted() throws Exception {
		when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		api.put(new OrderStatusUpdateDto(OrderStatus.PAID), "/orders/{orderId}/status", 42).andExpect(status().isAccepted());
	}
	
	@Test
	@OpenId("CASHIER")
	void whenCashierThenUpdateOrderStatusIsAccepted() throws Exception {
		when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		api.put(new OrderStatusUpdateDto(OrderStatus.PAID), "/orders/{orderId}/status", 42).andExpect(status().isAccepted());
	}
	
}
