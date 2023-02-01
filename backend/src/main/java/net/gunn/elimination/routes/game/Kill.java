package net.gunn.elimination.routes.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.gunn.elimination.model.EliminationUser;

import java.io.Serializable;

public record Kill(@JsonProperty EliminationUser eliminator, @JsonProperty EliminationUser eliminated) implements Serializable {
}
