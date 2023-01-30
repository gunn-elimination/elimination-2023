package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(using = BulkKillfeedSerializer.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public record BulkKillfeed(List<Kill> kills) implements KillEvent {

}
