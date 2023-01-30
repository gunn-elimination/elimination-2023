package net.gunn.elimination.routes.scoreboard;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record Scoreboard(@JsonProperty List<ScoreboardEntry> users) {
	public Scoreboard {
		users.sort(Collections.reverseOrder());
	}
}
