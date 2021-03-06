package com.lucadev.trampoline.security.logging.autoconfig;

import com.lucadev.trampoline.security.logging.UserActivity;
import com.lucadev.trampoline.security.logging.handler.LogUserActivityHandler;
import com.lucadev.trampoline.security.logging.handler.UserActivityHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 3/10/19
 */
public class UserActivityHandlerAutoConfigurationTest {

	private AnnotationConfigApplicationContext context;

	@Before
	public void setUp() throws Exception {
		context = new AnnotationConfigApplicationContext();
	}

	@After
	public void tearDown() throws Exception {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void registersUserActivityHandlerAutomatically() {
		this.context.register(UserActivityHandlerAutoConfiguration.class);
		this.context.refresh();

		UserActivityHandler userActivityHandler = context.getBean(UserActivityHandler.class);

		assertThat(userActivityHandler, instanceOf(LogUserActivityHandler.class));
	}

	@Test
	public void registerCustomUserActivityHandlerAutomatically() {
		this.context.register(CustomUserActivityHandlerConfig.class);
		this.context.register(UserActivityHandlerAutoConfiguration.class);
		this.context.refresh();

		UserActivityHandler userActivityHandler = context.getBean(UserActivityHandler.class);

		assertThat(userActivityHandler, instanceOf(CustomUserActivityHandler.class));
	}

	@Configuration
	protected static class CustomUserActivityHandlerConfig {
		@Bean
		public UserActivityHandler userActivityHandler() {
			return new CustomUserActivityHandler();
		}
	}

	protected static class CustomUserActivityHandler implements UserActivityHandler {

		@Override
		public void handleUserActivity(UserActivity userActivity) {

		}
	}

}