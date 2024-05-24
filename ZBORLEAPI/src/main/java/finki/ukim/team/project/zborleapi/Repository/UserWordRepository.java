package finki.ukim.team.project.zborleapi.Repository;

import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import finki.ukim.team.project.zborleapi.Model.UserWord;
import finki.ukim.team.project.zborleapi.Model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWordRepository extends JpaRepository<UserWord, Long> {
    UserWord findTopByUserAndUserCurrentWordIsTrue(User user);

    List<UserWord> findByUserAndCompletedIsFalse(User user);

    List<UserWord> findByUser(User user);

    @Modifying
    @Query("UPDATE UserWord uw SET uw.userCurrentWord = false WHERE uw.user = :user AND uw.userCurrentWord = true")
    void resetCurrentWordStatus(User user);

    @Query("SELECT uw FROM UserWord uw WHERE uw.user = :user AND uw.completed = true ORDER BY uw.completedAt DESC")
    List<UserWord> findLastPlayedWordsByUser(User user);
}