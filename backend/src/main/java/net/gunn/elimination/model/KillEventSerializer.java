package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

public class KillEventSerializer extends JsonSerializer<KillEvent> {
	ObjectMapper objectMapper = new ObjectMapper();

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

		Map eliminatorMap = objectMapper.convertValue(Hibernate.unproxy(kill.eliminator()), Map.class);
		Map eliminatedMap = objectMapper.convertValue(Hibernate.unproxy(kill.eliminated()), Map.class);

		gen.writeObjectField("eliminator", eliminatorMap);
		gen.writeObjectField("eliminated", eliminatedMap);
		gen.writeStringField("timeStamp", kill.timeStamp().toString());
		gen.writeEndObject();
	}
}
