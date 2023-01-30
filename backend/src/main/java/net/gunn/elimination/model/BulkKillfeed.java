package net.gunn.elimination.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(using = BulkKillfeedSerializer.class)
public record BulkKillfeed(List<Kill> kills) implements KillEvent {

}
