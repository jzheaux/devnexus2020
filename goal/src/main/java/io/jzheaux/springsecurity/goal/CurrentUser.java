package io.jzheaux.springsecurity.goal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.core.annotation.CurrentSecurityContext;

@CurrentSecurityContext(expression="authentication.tokenAttributes['user_id']")
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
