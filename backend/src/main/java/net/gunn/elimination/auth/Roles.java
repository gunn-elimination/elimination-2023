package net.gunn.elimination.auth;

import net.gunn.elimination.model.Role;
import net.gunn.elimination.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Roles {
    public static final Role USER = new Role("ROLE_USER");
    public static final Role PLAYER = new Role("ROLE_PLAYER");
	public static final Role ADMIN = new Role("ROLE_ADMIN");
	public static final Role EXCLUDED = new Role("ROLE_EXCLUDED");

    private Roles() {
    }

    @Autowired
    void setupRoles(RoleRepository roleRepository) {
        if (!roleRepository.existsByName(Roles.USER.getAuthority()))
            roleRepository.save(Roles.USER);
        if (!roleRepository.existsByName(Roles.PLAYER.getAuthority()))
            roleRepository.save(Roles.PLAYER);
        if (!roleRepository.existsByName(Roles.ADMIN.getAuthority()))
            roleRepository.save(Roles.ADMIN);
    }
}
