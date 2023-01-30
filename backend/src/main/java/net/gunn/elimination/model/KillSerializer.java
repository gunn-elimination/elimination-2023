package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class KillSerializer extends JsonSerializer<Kill> {
	@Override
	public void serialize(Kill value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("eliminator", value.eliminator().getSubject());
		gen.writeStringField("eliminated", value.eliminated().getSubject());
		gen.writeStringField("timeStamp", value.timeStamp().toString());
		gen.writeEndObject();
	}
}
