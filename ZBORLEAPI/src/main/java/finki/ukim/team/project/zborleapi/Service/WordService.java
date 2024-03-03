package finki.ukim.team.project.zborleapi.Service;


import finki.ukim.team.project.zborleapi.Model.DTO.Request.WordRequest;
import finki.ukim.team.project.zborleapi.Model.Word;
import finki.ukim.team.project.zborleapi.Repository.WordRepository;
import finki.ukim.team.project.zborleapi.Service.ServiceInterface.IWordService;
import finki.ukim.team.project.zborleapi.Utils.Exception.InvalidWordIdException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordService implements IWordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public Word findby(Integer id) {
        return this.wordRepository
                .findById(id)
                .orElseThrow(
                        () -> new InvalidWordIdException(id)
                );
    }
    @Override
    public List<Word> findAll() {
        return this.wordRepository.findAll();
    }

    @Override
    public Word delete(Integer id) {
        Word word = findby(id);
        this.wordRepository.delete(word);
        return word;
    }

    @Override
    public Word create(WordRequest word) {
        Word newWord = new Word(word.getWord());
        this.wordRepository.save(newWord);
        return newWord;
    }

    @Override
    public Word update(Integer id, WordRequest newWord) {
        Word word = findby(id);
        word.setWord(newWord.getWord());
        this.wordRepository.save(word);
        return word;
    }

    @Override
    public void saveAll(List<Word> words) {
        this.wordRepository.saveAll(words);
    }
}
