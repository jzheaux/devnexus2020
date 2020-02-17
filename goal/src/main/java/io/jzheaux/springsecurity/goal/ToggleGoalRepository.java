package io.jzheaux.springsecurity.goal;

import java.util.UUID;

public interface ToggleGoalRepository {
	Goal toggle(String user, UUID goalId);
}
