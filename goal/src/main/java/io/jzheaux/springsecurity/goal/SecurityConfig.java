package io.jzheaux.springsecurity.goal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

	public SecurityConfig() {
		addTenant("one");
		addTenant("two");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(a -> a
						.anyRequest().authenticated())
				.oauth2ResourceServer(o -> o
						.authenticationManagerResolver(resolver()));
	}

	@PostMapping("/tenant/{tenant}")
	String addTenant(@PathVariable("tenant") String tenant) {
		String issuerUri = "http://idp:9999/auth/realms/" + tenant;
		this.authenticationManagers.put(issuerUri, jwt(issuerUri));
		return issuerUri;
	}

	@DeleteMapping("/tenant/{tenant}")
	String removeTenant(@PathVariable("tenant") String tenant) {
		String issuerUri = "http://idp:9999/auth/realms/" + tenant;
		this.authenticationManagers.remove(issuerUri);
		return issuerUri;
	}

	AuthenticationManager jwt(String issuer) {
		JwtDecoder jwt = JwtDecoders.fromIssuerLocation(issuer);
		JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwt);
		return provider::authenticate;
	}

	AuthenticationManagerResolver<HttpServletRequest> resolver() {
		return new JwtIssuerAuthenticationManagerResolver
				(this.authenticationManagers::get);
	}

	@Bean
	Supplier<String> tenant() {
		return TenantResolver::resolve;
	}
}
