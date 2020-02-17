package io.jzheaux.springsecurity.keeper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
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
				.mvcMatchers("/login").permitAll()
				.anyRequest().authenticated())
			.oauth2Login(o -> o.loginPage("/login"))
			.logout(l -> l.logoutSuccessHandler(handler))
			.csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
	}

	@GetMapping("/login")
	public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ClientRegistration client = this.clients.findByRegistrationId(request.getServerName());
		if (client == null) {
			response.setStatus(401);
		} else {
			response.sendRedirect("/oauth2/authorization/" + client.getRegistrationId());
		}
	}
}
