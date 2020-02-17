package io.jzheaux.springsecurity.goal;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

public class TenantResolver {
	public static String resolve() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() instanceof AbstractOAuth2TokenAuthenticationToken) {
			AbstractOAuth2TokenAuthenticationToken token = (AbstractOAuth2TokenAuthenticationToken) context.getAuthentication();
			return (String) token.getTokenAttributes().get("tenant_id");
		}
		return "all";
	}
}
