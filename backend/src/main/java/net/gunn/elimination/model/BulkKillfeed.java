package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

public record BulkKillfeed(List<Kill> kills) implements KillEvent {
	public BulkKillfeed {
		var kills_ = new ArrayList<Kill>();
		for (Kill kill : kills) {
			kills_.add((Kill)Hibernate.unproxy(kill));
		}
		kills = kills_;
	}
}
