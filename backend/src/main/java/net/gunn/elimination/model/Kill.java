package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Entity
@JsonSerialize(using = KillSerializer.class)
public non-sealed class Kill implements Serializable, KillEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private String id;

	@JsonProperty
	@ManyToOne
	private EliminationUser eliminator;

	@JsonProperty
	@OneToOne
	private EliminationUser eliminated;

	@JsonProperty
	private Instant timeStamp;

	public Kill() {}

	public Kill(EliminationUser eliminator, EliminationUser eliminated) {
		this.eliminator = eliminator;
		this.eliminated = eliminated;

		this.timeStamp = Instant.now();
	}

	public EliminationUser eliminated() {
		return eliminated;
	}

	public EliminationUser eliminator() {
		return eliminator;
	}

	public Instant timeStamp() {
		return timeStamp;
	}
}
