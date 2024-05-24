package finki.ukim.team.project.zborleapi.Service;

import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import finki.ukim.team.project.zborleapi.Model.DTO.Request.WordRequest;
import finki.ukim.team.project.zborleapi.Model.UserWord;
import finki.ukim.team.project.zborleapi.Model.Word;
import finki.ukim.team.project.zborleapi.Repository.UserWordRepository;
import finki.ukim.team.project.zborleapi.Repository.WordRepository;
import finki.ukim.team.project.zborleapi.Service.ServiceInterface.IWordService;
import finki.ukim.team.project.zborleapi.Utils.Exception.InvalidWordIdException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class WordService implements IWordService {

    private final WordRepository wordRepository;
    private final UserWordRepository userWordRepository;

    public WordService(WordRepository wordRepository, UserWordRepository userWordRepository) {
        this.wordRepository = wordRepository;
        this.userWordRepository = userWordRepository;
    }

    @Override
    public Word findby(Integer id) {
        return this.wordRepository.findById(id)
                .orElseThrow(() -> new InvalidWordIdException(id));
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

    @Async
    public void assignWordsToUser(User user) {
        List<Word> words = wordRepository.findAll();
        Random rand = new Random();
        int randomNumber = rand.nextInt(words.size());
        int index = 0;
        for (Word word : words) {
            UserWord userWord = new UserWord();
            userWord.setUser(user);
            userWord.setWord(word);
            userWord.setNumberOfAttempts(0);
            userWord.setUserCurrentWord(false); // Initially, no word is the current word
            userWord.setCompleted(false); // Initially, no word is completed
            userWord.setWon(false); // Initially, no word is won
            if (randomNumber == index) {
                userWord.setUserCurrentWord(true); // Set a random word as the current word
                userWord.setStartedAt(LocalDateTime.now()); // Set the start time for the current word
                userWord.setGameDate(LocalDate.now()); // Set the game date for the current word
            }
            userWordRepository.save(userWord);
            index++;
        }
    }
}
