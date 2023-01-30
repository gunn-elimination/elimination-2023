package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = KillEventSerializer.class)
public sealed interface KillEvent permits Kill, BulkKillfeed {

}
