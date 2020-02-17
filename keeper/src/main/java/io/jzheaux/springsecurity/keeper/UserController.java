package io.jzheaux.springsecurity.keeper;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@GetMapping("/user")
	String user(@AuthenticationPrincipal OidcUser user) {
		return user.getGivenName();
	}
}
