package net.gunn.elimination.auth;

import net.gunn.elimination.routes.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class RoleConfig {
	private final UserRepository userRepository;

	public RoleConfig(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Bean
	public AdminList adminList(@Value("${elimination.admins}") String admins) {
		var emails = new HashSet<String>();
		for (var admin : admins.split(",")) {
			admin = admin.trim();
			for (var user : userRepository.findByForenameAndSurname(admin.split(" ")[0], admin.split(" ")[1])) {
				emails.add(user.getEmail());
			}
		}
		return new AdminList(emails);
	}
}
