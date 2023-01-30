package net.gunn.elimination.model;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;

@Entity
public class Role implements Serializable, GrantedAuthority {
    @Column
    @Id
    private String name;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Role && ((Role) obj).name.equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

	@Override
	public String getAuthority() {
		return name;
	}
}

