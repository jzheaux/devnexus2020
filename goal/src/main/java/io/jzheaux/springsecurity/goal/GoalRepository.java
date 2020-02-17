package io.jzheaux.springsecurity.goal;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends CrudRepository<Goal, String>, ToggleGoalRepository {
	@Query("{ 'user' : ?#{authentication.tokenAttributes['user_id']} }")
	@Override
	Iterable<Goal> findAll();
}
