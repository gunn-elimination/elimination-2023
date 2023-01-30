package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
@JsonSubTypes(
	{
		@JsonSubTypes.Type(value = Kill.class, name = "Kill"),
		@JsonSubTypes.Type(value = BulkKillfeed.class, name = "BulkKillfeed")
	}
)
public sealed interface KillEvent permits Kill, BulkKillfeed {

}
