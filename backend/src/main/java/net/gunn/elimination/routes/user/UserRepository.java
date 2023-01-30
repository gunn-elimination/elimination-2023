package net.gunn.elimination.routes.user;

import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Repository
public interface UserRepository extends JpaRepository<EliminationUser, Long> {
	boolean existsBySubject(String subject);

	Optional<EliminationUser> findBySubject(String subject);

	Optional<EliminationUser> findByEmail(String email);

	void deleteBySubject(String subject);

	Page<EliminationUser> findEliminationUsersByRolesContainingAndSubjectNot(Role role, String blacklistedSubject, Pageable pageable);

	Set<EliminationUser> findByForenameAndSurname(String forename, String surname);

	Optional<EliminationUser> findByWinnerTrue();
}
