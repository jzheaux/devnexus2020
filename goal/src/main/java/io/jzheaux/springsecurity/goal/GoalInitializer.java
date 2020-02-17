package io.jzheaux.springsecurity.goal;

import java.util.Collections;
import java.util.function.Consumer;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
class GoalInitializer implements SmartInitializingSingleton {
	private final GoalRepository goals;

	GoalInitializer(GoalRepository goals) {
		this.goals = goals;
	}

	@Override
	public void afterSingletonsInstantiated() {
		String rob = "6fb5a754-c666-4859-9452-f885796ee73e";
		String joe = "94d835cc-c70f-47c1-8206-2ad7c8a37565";
		String josh = "219168d2-1da4-4f8a-85d8-95b4377af3c1";
		String ria = "3df7633b-0375-4609-a256-93bab5d19762";

		String one = "one";
		String two = "two";

		this.goals.save(new Goal(rob, "Attend Chiefs game"));
		this.goals.save(new Goal(joe, "Add OAuth 2.0 support to Spring Security"));
		this.goals.save(new Goal(joe, "Tell Josh what to do"));
		this.goals.save(new Goal(josh, "Be a good data steward"));
		this.goals.save(new Goal(josh, "Try chicken and waffles on a stick"));

		this.goals.save(new Goal(josh, "Add multi-tenancy to Goalkeeper"));
		this.goals.save(new Goal(josh, "Eat some mini-wheats"));
		this.goals.save(new Goal(ria, "Give an awesome talk"));
		this.goals.save(new Goal(ria, "Enjoy the rest of the conference"));
	}

	private void withTenant(String tenant, Consumer<String> todo) {
		Jwt jwt = Jwt.withTokenValue("token")
				.header("alg", "none")
				.claim("tenant_id", tenant).build();
		JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, Collections.emptyList());
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		todo.accept(tenant);
		SecurityContextHolder.clearContext();
	}
}
