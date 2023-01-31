package net.gunn.elimination.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

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
public non-sealed class Kill implements Serializable, KillEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@JsonProperty
	@ManyToOne
	@LazyToOne(value = LazyToOneOption.NO_PROXY)
	private EliminationUser eliminator;

	@JsonProperty
	@OneToOne
	@LazyToOne(value = LazyToOneOption.NO_PROXY)
	private EliminationUser eliminated;

	@JsonProperty
	private Instant timeStamp;

	public Kill() {}

	public Kill(EliminationUser eliminator, EliminationUser eliminated, Instant timeStamp) {
		this.eliminator = (EliminationUser) Hibernate.unproxy(eliminator);
		this.eliminated = (EliminationUser) Hibernate.unproxy(eliminated);

		this.timeStamp = timeStamp;
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
