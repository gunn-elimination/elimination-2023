package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

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

	public void addKill(JsonGenerator gen, Kill kill) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Hibernate5Module());

		gen.writeStartObject();

		Hibernate.initialize(kill);
		Hibernate.initialize(kill.eliminator());
		Hibernate.initialize(kill.eliminated());

		EliminationUser eliminator = kill.eliminator();
		EliminationUser eliminated = kill.eliminated();

		Map eliminatorMap = objectMapper.convertValue(eliminator, Map.class);
		Map eliminatedMap = objectMapper.convertValue(eliminated, Map.class);

		gen.writeObjectField("eliminator", eliminatorMap);
		gen.writeObjectField("eliminated", eliminatedMap);
		gen.writeStringField("timeStamp", kill.timeStamp().toString());
		gen.writeEndObject();
	}
}
