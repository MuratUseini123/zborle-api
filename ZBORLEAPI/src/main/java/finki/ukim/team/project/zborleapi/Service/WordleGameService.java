package finki.ukim.team.project.zborleapi.Service;

import finki.ukim.team.project.zborleapi.Model.Answer;
import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.GameFeedback;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserGuessResponse;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserStatistics;
import finki.ukim.team.project.zborleapi.Model.UserWord;
import finki.ukim.team.project.zborleapi.Model.Word;
import finki.ukim.team.project.zborleapi.Repository.UserRepository;
import finki.ukim.team.project.zborleapi.Repository.UserWordRepository;
import finki.ukim.team.project.zborleapi.Repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WordleGameService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserWordRepository userWordRepository;

    @Autowired
    private WordRepository wordRepository;

    @Transactional
    public GameFeedback checkWord(String guess) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByEmail(username).orElse(null);
        UserWord userWord = userWordRepository.findTopByUserAndUserCurrentWordIsTrue(user);
        String target = userWord.getWord().getWord();

        List<UserGuessResponse> userGuessResponse = new ArrayList<>();
        String message;
        userWord.setNumberOfAttempts(userWord.getNumberOfAttempts() + 1);
        userWord.getGuesses().add(guess);  // Save the guess

        if (guess.equals(target)) {
            message = "You found it!";
            userWord.setCompleted(true);
            userWord.setWon(true);
            userWord.setCompletedAt(LocalDateTime.now());
            userWord.setAttempts(userWord.getNumberOfAttempts());
            userWordRepository.save(userWord);
            for (int i = 0; i < guess.length(); i++) {
                userGuessResponse.add(new UserGuessResponse(String.valueOf(guess.charAt(i)), Answer.CORRECT, i));
            }
            assignNewWord(user);  // Assign a new word after winning
        } else {
            for (int i = 0; i < guess.length(); i++) {
                char guessChar = guess.charAt(i);
                char targetChar = target.charAt(i);
                Answer answer;
                if (guessChar == targetChar) {
                    answer = Answer.CORRECT;
                } else if (target.contains(String.valueOf(guessChar))) {
                    answer = Answer.PARTIALLY_CORRECT;
                } else {
                    answer = Answer.NOT_CORRECT;
                }
                userGuessResponse.add(new UserGuessResponse(String.valueOf(guessChar), answer, i));
            }
            if (userWord.getNumberOfAttempts() >= 6) {
                message = "Game over! The correct word was " + target;
                userWord.setCompleted(true);
                userWord.setCompletedAt(LocalDateTime.now());
                userWord.setAttempts(userWord.getNumberOfAttempts());
                userWordRepository.save(userWord);
                assignNewWord(user);  // Assign a new word after losing
            } else {
                message = "Try again!";
            }
        }

        userWordRepository.save(userWord);
        return new GameFeedback(userGuessResponse, message, userWord.getGuesses());
    }

    @Transactional
    public UserWord startOrContinueGame(User user) {
        UserWord currentGame = userWordRepository.findTopByUserAndUserCurrentWordIsTrue(user);
        if (currentGame != null) {
            return currentGame;
        }

        // Mark all previous incomplete games as completed
        List<UserWord> userWords = userWordRepository.findByUserAndCompletedIsFalse(user);
        for (UserWord userWord : userWords) {
            userWord.setCompleted(true);
            userWord.setCompletedAt(LocalDateTime.now());
            userWordRepository.save(userWord);
        }

        // Reset the current word status for all previous words
        userWordRepository.resetCurrentWordStatus(user);

        // Assign a new word for today's game
        List<Word> words = wordRepository.findAll();
        Word newWord = words.get(new Random().nextInt(words.size()));

        UserWord newUserWord = new UserWord();
        newUserWord.setUser(user);
        newUserWord.setWord(newWord);
        newUserWord.setNumberOfAttempts(0);
        newUserWord.setUserCurrentWord(true);
        newUserWord.setStartedAt(LocalDateTime.now());
        newUserWord.setGameDate(LocalDate.now());
        newUserWord.setCompleted(false);
        newUserWord.setWon(false);
        userWordRepository.save(newUserWord);

        return newUserWord;
    }

    public UserWord loadLastGame(User user) {
        return userWordRepository.findTopByUserAndUserCurrentWordIsTrue(user);
    }

    public List<UserWord> getStats(User user) {
        return userWordRepository.findLastPlayedWordsByUser(user);
    }

    public UserStatistics getUserStatistics(User user) {
        List<UserWord> userWords = userWordRepository.findByUser(user);
        long gamesPlayed = userWords.stream().filter(UserWord::isCompleted).count();
        long gamesWon = userWords.stream().filter(UserWord::isWon).count();
        double winPercentage = 0;
        if(gamesPlayed!=0) {
            winPercentage = (gamesWon / (double) gamesPlayed) * 100;
        }
        double averageAttempts = userWords.stream().filter(UserWord::isCompleted).mapToInt(UserWord::getAttempts).average().orElse(0);
        return new UserStatistics(gamesPlayed, gamesWon, winPercentage, averageAttempts);
    }

    private void assignNewWord(User user) {
        // Reset the current word status for all previous words
        userWordRepository.resetCurrentWordStatus(user);

        // Assign a new word
        List<Word> words = wordRepository.findAll();
        Word newWord = words.get(new Random().nextInt(words.size()));

        UserWord newUserWord = new UserWord();
        newUserWord.setUser(user);
        newUserWord.setWord(newWord);
        newUserWord.setNumberOfAttempts(0);
        newUserWord.setUserCurrentWord(true);
        newUserWord.setStartedAt(LocalDateTime.now());
        newUserWord.setGameDate(LocalDate.now());
        newUserWord.setCompleted(false);
        newUserWord.setWon(false);
        userWordRepository.save(newUserWord);
    }
}