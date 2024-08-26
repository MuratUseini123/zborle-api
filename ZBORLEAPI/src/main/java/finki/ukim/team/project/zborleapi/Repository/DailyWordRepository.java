package finki.ukim.team.project.zborleapi.Repository;

import finki.ukim.team.project.zborleapi.Model.DailyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyWordRepository extends JpaRepository<DailyWord, Long> {
    Optional<DailyWord> findByDate(LocalDate date);
}