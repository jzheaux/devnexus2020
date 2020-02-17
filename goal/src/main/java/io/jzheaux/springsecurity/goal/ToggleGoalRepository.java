package io.jzheaux.springsecurity.goal;

import java.util.UUID;

public interface ToggleGoalRepository {
	Goal toggle(String tenant, String user, UUID goalId);
}
