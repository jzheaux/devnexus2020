package io.jzheaux.springsecurity.keeper;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@RestController
public class GoalController {
	private final WebClient goals;

	public GoalController(WebClient goals) {
		this.goals = goals;
	}

	@GetMapping("/goals")
	List<Goal> goals() {
		return this.goals
				.get().uri("/goals")
				.retrieve()
				.bodyToFlux(Goal.class)
				.collectList()
				.block();
	}

	@PostMapping("/goal")
	Goal add(@RequestBody String text) {
		return this.goals
				.post().uri("/goal")
				.body(fromValue(text))
				.retrieve()
				.bodyToMono(Goal.class)
				.block();
	}

	@PutMapping("/goal/{id}/toggle")
	Goal toggle(@PathVariable("id") String goalId) {
		return this.goals
				.put().uri("/goal/" + goalId + "/toggle")
				.retrieve()
				.bodyToMono(Goal.class)
				.block();
	}
}
