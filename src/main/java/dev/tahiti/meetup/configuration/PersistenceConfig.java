package dev.tahiti.meetup.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dev.tahiti.meetup.domain.Drink;
import dev.tahiti.meetup.domain.Order;
import dev.tahiti.meetup.domain.Table;
import dev.tahiti.meetup.jpa.DrinkRepository;
import dev.tahiti.meetup.jpa.OrderRepository;
import dev.tahiti.meetup.jpa.TableRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = { Order.class })
@EnableJpaRepositories(basePackageClasses = { OrderRepository.class })
public class PersistenceConfig {
	
	@Component
	@RequiredArgsConstructor
	static class DbInit implements InitializingBean {
		private final TableRepository tableRepo;
		private final DrinkRepository drinkRepo;

		@Override
		public void afterPropertiesSet() throws Exception {
			tableRepo.save(new Table("One"));
			tableRepo.save(new Table("Two"));
			tableRepo.save(new Table("Three"));

			drinkRepo.save(new Drink("Guinness"));
			drinkRepo.save(new Drink("Mojito"));
			drinkRepo.save(new Drink("Citronade"));
		}
		
	}

}
