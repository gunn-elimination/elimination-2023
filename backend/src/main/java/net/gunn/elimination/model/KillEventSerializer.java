package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class KillEventSerializer extends JsonSerializer<KillEvent> {
	@Override
	public void serialize(KillEvent value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		if (value instanceof Kill kill) {
			gen.writeStringField("type", "kill");

			gen.writeFieldName("value");
			gen.writeStartObject();
			gen.writeStringField("eliminator", kill.eliminator().getSubject());
			gen.writeStringField("eliminated", kill.eliminated().getSubject());
			gen.writeStringField("timeStamp", kill.timeStamp().toString());
			gen.writeEndObject();
		} else if (value instanceof BulkKillfeed bk) {
			gen.writeStringField("type", "bulkKillfeed");

			gen.writeFieldName("value");
			gen.writeStartArray();
			for (Kill kill : bk.kills()) {
				gen.writeObject(kill);
			}
			gen.writeEndArray();
		}

		gen.writeEndObject();
	}
}
