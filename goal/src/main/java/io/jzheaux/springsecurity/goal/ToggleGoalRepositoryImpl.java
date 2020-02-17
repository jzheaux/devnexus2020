package io.jzheaux.springsecurity.goal;

import java.util.UUID;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class ToggleGoalRepositoryImpl implements ToggleGoalRepository {
	private final MongoTemplate mongo;

	public ToggleGoalRepositoryImpl(MongoTemplate mongo) {
		this.mongo = mongo;
	}

	@Override
	public Goal toggle(String user, UUID goalId) {
		String tenant = TenantResolver.resolve();
		return this.mongo.findAndModify(
				query(where("id").is(goalId).and("tenant").is(tenant).and("user").is(user)),
				new Update().bitwise("completed").xor(1),
				FindAndModifyOptions.options().returnNew(true),
				Goal.class);
	}
}
