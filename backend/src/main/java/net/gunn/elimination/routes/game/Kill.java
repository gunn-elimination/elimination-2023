package net.gunn.elimination.routes.game;

import net.gunn.elimination.model.EliminationUser;

public record Kill(EliminationUser eliminator, EliminationUser eliminated) {
}
