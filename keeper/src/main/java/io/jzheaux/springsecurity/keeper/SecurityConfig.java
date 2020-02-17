package io.jzheaux.springsecurity.keeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	ClientRegistrationRepository clients;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		OidcClientInitiatedLogoutSuccessHandler handler =
				new OidcClientInitiatedLogoutSuccessHandler(this.clients);
		handler.setPostLogoutRedirectUri("{baseUrl}");

		http
			.authorizeRequests(a -> a
				.anyRequest().authenticated())
			.oauth2Login(Customizer.withDefaults())
			.logout(l -> l.logoutSuccessHandler(handler))
			.csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
	}
}
