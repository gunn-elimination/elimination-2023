package net.gunn.elimination.routes.scoreboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.gunn.elimination.model.EliminationUser;

import java.util.List;

public record ScoreboardEntry(EliminationUser user, long numEliminations) {
}
