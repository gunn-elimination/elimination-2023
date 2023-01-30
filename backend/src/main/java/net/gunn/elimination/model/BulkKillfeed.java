package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

public record BulkKillfeed(List<Kill> kills) implements KillEvent {

}
