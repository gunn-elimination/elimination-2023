package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

public class BulkKillfeedSerializer extends JsonSerializer<BulkKillfeed> {
	@Override
	public void serialize(BulkKillfeed value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartArray();
		for (Kill kill : value.kills()) {
			gen.writeObject(kill);
		}
		gen.writeEndArray();
	}
}
