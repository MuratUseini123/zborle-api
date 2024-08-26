package finki.ukim.team.project.zborleapi.Repository;

import finki.ukim.team.project.zborleapi.Model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
}