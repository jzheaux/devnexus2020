package io.jzheaux.springsecurity.goal;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
	@Bean
	Supplier<String> tenant() {
		return TenantResolver::resolve;
	}
}
