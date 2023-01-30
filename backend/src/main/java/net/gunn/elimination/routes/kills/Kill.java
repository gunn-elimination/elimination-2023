package net.gunn.elimination.routes.kills;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.gunn.elimination.model.EliminationUser;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public final class Kill implements Serializable {
//	@Serial
//	private static final long serialVersionUID = 0L;

	@Id
	@GeneratedValue
	public long id;

	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	public EliminationUser eliminator;

	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	public EliminationUser eliminated;

	public Kill() {}

	public Kill(EliminationUser eliminator, EliminationUser eliminated) {
		this.eliminator = eliminator;
		this.eliminated = eliminated;
	}


}
