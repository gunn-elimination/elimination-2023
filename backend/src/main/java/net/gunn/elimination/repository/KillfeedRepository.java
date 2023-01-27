package net.gunn.elimination.repository;

import net.gunn.elimination.model.Kill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface KillfeedRepository extends JpaRepository<Kill, Long> {

}
