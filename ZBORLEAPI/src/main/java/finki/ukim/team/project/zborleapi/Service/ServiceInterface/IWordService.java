package finki.ukim.team.project.zborleapi.Service.ServiceInterface;


import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import finki.ukim.team.project.zborleapi.Model.DTO.Request.WordRequest;
import finki.ukim.team.project.zborleapi.Model.Word;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IWordService {
    Word findby(Integer id);
    List<Word> findAll();
    Word delete(Integer id);
    Word create (WordRequest word);
    Word update(Integer id, WordRequest newWord);
    void saveAll(List<Word> words);

    void assignWordsToUser(User user);
}
