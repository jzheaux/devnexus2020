package io.jzheaux.springsecurity.goal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

@SpringBootApplication
public class GoalApplication implements ApplicationListener<AbstractAuthenticationFailureEvent> {

	@Override
	public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
		System.out.println(event.getException().getMessage());
	}

	public static void main(String[] args) {
		SpringApplication.run(GoalApplication.class, args);
	}

}
