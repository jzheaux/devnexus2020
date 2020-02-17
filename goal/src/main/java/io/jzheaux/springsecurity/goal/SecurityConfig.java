package io.jzheaux.springsecurity.goal;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Suppliers;
import com.nimbusds.jwt.JWTParser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
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
		Supplier<AuthenticationManager> tokenType = Suppliers.memoize(this::tokenType);
		return request -> tokenType.get();
	}

	AuthenticationManager tokenType() {
		AuthenticationManager jwt = jwt();
		AuthenticationManager opaqueToken = opaqueToken();
		return authentication ->
				isAJwt((BearerTokenAuthenticationToken) authentication) ?
						jwt.authenticate(authentication) :
						opaqueToken.authenticate(authentication);
	}

	boolean isAJwt(BearerTokenAuthenticationToken token) {
		try {
			JWTParser.parse(token.getToken());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	AuthenticationManager jwt() {
		JwtDecoder jwt = JwtDecoders.fromIssuerLocation("http://idp:9999/auth/realms/one");
		JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwt);
		return provider::authenticate;
	}

	AuthenticationManager opaqueToken() {
		OpaqueTokenIntrospector introspector = new NimbusOpaqueTokenIntrospector(
				"http://idp:9999/auth/realms/two/protocol/openid-connect/token/introspect",
				"keeper",
				"bfbd9f62-02ce-4638-a370-80d45514bd0a");
		OpaqueTokenAuthenticationProvider provider = new OpaqueTokenAuthenticationProvider(introspector);
		return provider::authenticate;
	}

	@Bean
	Supplier<String> tenant() {
		return TenantResolver::resolve;
	}
}
