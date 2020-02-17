package io.jzheaux.springsecurity.goal;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Suppliers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(a -> a
						.anyRequest().authenticated())
				.oauth2ResourceServer(o -> o
						.authenticationManagerResolver(resolver()));
	}

	AuthenticationManagerResolver<HttpServletRequest> resolver() {
		Supplier<AuthenticationManager> jwt = Suppliers.memoize(this::jwt);
		return request -> jwt.get();
	}

	AuthenticationManager jwt() {
		JwtDecoder jwt = JwtDecoders.fromIssuerLocation("http://idp:9999/auth/realms/one");
		JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwt);
		return provider::authenticate;
	}

	@Bean
	Supplier<String> tenant() {
		return TenantResolver::resolve;
	}
}
