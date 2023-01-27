package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Entity
public class Kill implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private String id;

	@JsonProperty
	private String eliminator;

	@JsonProperty
	private String eliminated;

	@JsonProperty
	private String timeStamp;

	public Kill() {}

	public Kill(EliminationUser eliminator, EliminationUser eliminated) {
		this.eliminator = eliminator.getSubject();
		this.eliminated = eliminated.getSubject();

		this.timeStamp = ZonedDateTime.now(ZoneId.of("America/Los_Angeles")).format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public String getEliminator() {
		return eliminator;
	}

	public String getEliminated() {
		return eliminated;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
}
