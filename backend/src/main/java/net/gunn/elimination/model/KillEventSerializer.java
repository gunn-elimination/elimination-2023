package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public class KillEventSerializer extends JsonSerializer<KillEvent> {
	@Override
	@Transactional
	public void serialize(KillEvent value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		if (value instanceof Kill kill) {
			gen.writeStringField("type", "kill");

			gen.writeFieldName("value");
			addKill(gen, kill);
		} else if (value instanceof BulkKillfeed bk) {
			gen.writeStringField("type", "bulkKillfeed");

			gen.writeFieldName("value");
			gen.writeStartArray();
			for (Kill kill : bk.kills()) {
				addKill(gen, kill);
			}
			gen.writeEndArray();
		}

		gen.writeEndObject();
	}

	@Transactional
	void addKill(JsonGenerator gen, Kill kill) throws IOException {
		gen.writeStartObject();
		gen.writeObjectField("eliminator", kill.eliminator());
		gen.writeObjectField("eliminated", kill.eliminated());
		gen.writeStringField("timeStamp", kill.timeStamp().toString());
		gen.writeEndObject();
	}
}
