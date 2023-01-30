package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PublicUserSerialiser extends JsonSerializer<EliminationUser> {
	@Override
	public void serialize(EliminationUser user, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("subject", user.getSubject());
		gen.writeStringField("email", user.getEmail());
		gen.writeStringField("forename", user.getForename());
		gen.writeStringField("surname", user.getSurname());
		gen.writeArray(
			user.eliminated().stream()
				.map(EliminationUser::getSubject)
				.toArray(String[]::new), 0, user.eliminated().size()
		);
		gen.writeBooleanField("winner", user.isWinner());
		gen.writeBooleanField("alive", user.isEliminated());
		if (user.isEliminated())
			gen.writeStringField("eliminatedBy", user.getEliminatedBy().getSubject());

		gen.writeEndObject();
	}
}
