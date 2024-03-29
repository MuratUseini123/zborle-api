package finki.ukim.team.project.zborleapi.Repository;

import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
