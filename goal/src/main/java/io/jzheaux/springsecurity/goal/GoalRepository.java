package io.jzheaux.springsecurity.goal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends CrudRepository<Goal, String>, ToggleGoalRepository {
	Iterable<Goal> findByUser(String user);
}
