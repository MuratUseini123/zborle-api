package finki.ukim.team.project.zborleapi.Repository;


import finki.ukim.team.project.zborleapi.Model.UserGuess;
import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGuessRepository extends JpaRepository<UserGuess, Long> {
    List<UserGuess> findByUser(User user);
}