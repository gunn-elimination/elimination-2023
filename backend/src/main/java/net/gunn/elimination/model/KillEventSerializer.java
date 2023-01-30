package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class KillEventSerializer extends JsonSerializer<KillEvent> {
	@Override
	public void serialize(KillEvent value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		if (value instanceof Kill kill) {
			gen.writeStringField("type", "kill");

			gen.writeFieldName("value");
			gen.writeStartObject();
			gen.writeObjectField("eliminator", kill.eliminator().decompose());
			gen.writeObjectField("eliminated", kill.eliminated().decompose());
			gen.writeStringField("timeStamp", kill.timeStamp().toString());
			gen.writeEndObject();
		} else if (value instanceof BulkKillfeed bk) {
			gen.writeStringField("type", "bulkKillfeed");

			gen.writeFieldName("value");
			gen.writeStartArray();
			for (Kill kill : bk.kills()) {
				gen.writeStartObject();
				gen.writeObjectField("eliminator", kill.eliminator().decompose());
				gen.writeObjectField("eliminated", kill.eliminated().decompose());
				gen.writeStringField("timeStamp", kill.timeStamp().toString());
				gen.writeEndObject();
			}
			gen.writeEndArray();
		}

		gen.writeEndObject();
	}
}
