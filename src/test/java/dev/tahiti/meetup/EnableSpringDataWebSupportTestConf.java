package dev.tahiti.meetup;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.tahiti.meetup.domain.Order;
import dev.tahiti.meetup.jpa.DrinkRepository;
import dev.tahiti.meetup.jpa.OrderRepository;
import dev.tahiti.meetup.jpa.TableRepository;

/**
 * Avoid MethodArgumentConversionNotSupportedException with repos MockBean
 *
 * @author Jérôme Wacongne &lt;ch4mp#64;c4-soft.com&gt;
 */
@TestConfiguration
public class EnableSpringDataWebSupportTestConf {
	@Bean
	WebMvcConfigurer configurer(DrinkRepository drinkRepo, TableRepository tableRepo, OrderRepository orderRepo) {
		return new WebMvcConfigurer() {
			@Override
			public void addFormatters(FormatterRegistry registry) {
				registry.addConverter(String.class, Order.class, id -> orderRepo.findById(Long.valueOf(id)).get());
			}
		};
	}
}