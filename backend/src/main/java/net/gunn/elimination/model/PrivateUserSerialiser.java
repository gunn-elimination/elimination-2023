package net.gunn.elimination.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.routes.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

@Component
public class PrivateUserSerialiser extends JsonSerializer<EliminationAuthentication> {
	@Autowired
	private UserRepository userRepository;

	public PrivateUserSerialiser() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void serialize(EliminationAuthentication token, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		var user = userRepository.findBySubject(token.subject()).orElseThrow();

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
		else {
			gen.writeStringField("target", user.getTarget().getSubject());
			gen.writeStringField("eliminationCode", user.getEliminationCode());
		}

		gen.writeEndObject();
	}
}
