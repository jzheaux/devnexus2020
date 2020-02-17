package io.jzheaux.springsecurity.goal;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoalController {
	private final GoalRepository goals;

	public GoalController(GoalRepository goals) {
		this.goals = goals;
	}

	@GetMapping("/goals")
	Iterable<Goal> goals(@RequestHeader("tenant") String tenant, @CurrentUser String user) {
		return this.goals.findByTenantAndUser(tenant, user);
	}

	@PostMapping("/goal")
	Goal goal(@RequestHeader("tenant") String tenant, @CurrentUser String user, @RequestBody String text) {
		Goal goal = new Goal(tenant, user, text);
		return this.goals.save(goal);
	}

	@PutMapping("/goal/{id}/toggle")
	Goal toggle(@RequestHeader("tenant") String tenant, @CurrentUser String user, @PathVariable("id") UUID goalId) {
		return this.goals.toggle(tenant, user, goalId);
	}
}
