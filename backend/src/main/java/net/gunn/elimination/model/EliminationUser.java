package net.gunn.elimination.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@JsonSerialize(using = PublicUserSerialiser.class)
/**
 * this is a fat and ugly class that is used to represent a user in the database.
 * it is a combination of the OidcUser and the UserDetails interface, and does way too much.
 * but it's nice because our session info is stored in the database, and we don't want to
 * have to do a bunch of extra work to get the session info.
 */
public class EliminationUser implements Serializable {
    @Id
    @Column(columnDefinition = "TEXT")
    private String subject;

    @Column
    private String email;

    @Column
    private String forename, surname;

    @OneToOne(mappedBy = "target", fetch = FetchType.LAZY)
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    private EliminationUser targettedBy;

    @OneToOne(fetch = FetchType.LAZY)
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    private EliminationUser target;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    private EliminationUser eliminatedBy;

    @OneToMany(mappedBy = "eliminatedBy", fetch = FetchType.EAGER)
    private Set<EliminationUser> eliminated = ConcurrentHashMap.newKeySet();

    @Column
    private boolean winner = false;

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public void setEliminationCode(String eliminationCode) {
        this.eliminationCode = eliminationCode;
    }

    public Set<EliminationUser> getPreviousVictims() {
        return eliminated;
    }

    public EliminationUser getEliminatedBy() {
        return eliminatedBy;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = ConcurrentHashMap.newKeySet();

    @Column
    private String eliminationCode;

    public EliminationUser() {
    }


    public EliminationUser(String subject, String email, String forename, String surname, String eliminationCode, Set<Role> roles
    ) {
        this.subject = subject;
        this.email = email;
        this.forename = forename;
        this.surname = surname;
        this.eliminationCode = eliminationCode;
        this.roles = roles == null ? ConcurrentHashMap.newKeySet() : roles;
    }

    public String getSubject() {
        return subject;
    }

    public String getEmail() {
        return email;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public EliminationUser getTarget() {
        return target;
    }

    public EliminationUser getTargettedBy() {
        return targettedBy;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTarget(EliminationUser target) {
        this.target = target;
    }

    public void setTargettedBy(EliminationUser targettedBy) {
        this.targettedBy = targettedBy;
    }

    public String getEliminationCode() {
        return eliminationCode;
    }

    public boolean isEliminated() {
        assert (target == null && targettedBy == null && eliminationCode == null) || (target != null && targettedBy != null && eliminationCode != null);

        return eliminatedBy != null;
    }

	public Set<EliminationUser> eliminated() {
		return eliminated;
	}

	public void setEliminatedBy(EliminationUser eliminatedBy) {
        this.eliminatedBy = eliminatedBy;
    }

    public boolean addRole(Role role) {
        this.roles = new HashSet<>(this.roles);
        return this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles = new HashSet<>(this.roles) {{
            remove(role);
        }};
    }

    @Override
    public String toString() {
        return getForename() + " " + getSurname() + " (" + getEmail() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EliminationUser && ((EliminationUser) obj).getSubject().equals(getSubject());
    }
}
