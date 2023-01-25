package net.gunn.elimination.repository;

import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.model.Role;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Repository
@Cacheable("users")
public interface UserRepository extends JpaRepository<EliminationUser, Long> {
	boolean existsBySubject(String subject);

	Optional<EliminationUser> findBySubject(String subject);

	Optional<EliminationUser> findByEmail(String email);

	void deleteBySubject(String subject);

	Page<EliminationUser> findEliminationUsersByRolesContainingAndSubjectNot(Role role, String blacklistedSubject, Pageable pageable);

	@Query("select u from EliminationUser u order by u.eliminated.size desc")
	Stream<EliminationUser> findTopByNumberOfEliminations();

	// return limited list. with limit as param
	@Query("select u from EliminationUser u order by u.eliminated.size desc")
	@Cacheable("scoreboard")
	List<EliminationUser> findTopByNumberOfEliminations(Pageable pageable);

	@Override
	@CacheEvict(value = {"scoreboard", "users"}, allEntries = true)
	<S extends EliminationUser> S save(S entity);

	Optional<EliminationUser> findByWinnerTrue();
}
