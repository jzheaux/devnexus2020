package io.jzheaux.springsecurity.goal;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Goal {
	@Id
	private UUID id;

	private String user;

	private String text;

	private Integer completed = 0;

	public Goal(String user, String text) {
		this.id = UUID.randomUUID();
		this.user = user;
		this.text = text;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getCompleted() {
		return completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}
}
