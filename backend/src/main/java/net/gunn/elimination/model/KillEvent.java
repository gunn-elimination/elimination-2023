package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes(
	{
		@JsonSubTypes.Type(value = Kill.class, name = "Kill"),
		@JsonSubTypes.Type(value = BulkKillfeed.class, name = "BulkKillfeed")
	}
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public sealed interface KillEvent permits Kill, BulkKillfeed {

}
