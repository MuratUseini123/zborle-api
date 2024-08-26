package finki.ukim.team.project.zborleapi.Repository;

import finki.ukim.team.project.zborleapi.Model.UserGuess;
import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserGuessRepository extends JpaRepository<UserGuess, Long> {

    // Retrieve all guesses made by a user
    List<UserGuess> findByUser(User user);

    // Retrieve all guesses made by a user within a specific time range
    List<UserGuess> findByUserAndCreatedAtBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Retrieve all guesses for a specific word (optional)
    List<UserGuess> findByUserAndGuess(User user, String guess);
}