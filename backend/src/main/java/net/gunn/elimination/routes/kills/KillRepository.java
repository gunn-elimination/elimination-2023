package net.gunn.elimination.routes.kills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface KillRepository extends JpaRepository<Kill, Long> {

}
