package finki.ukim.team.project.zborleapi.Service;

import finki.ukim.team.project.zborleapi.Model.Answer;
import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import finki.ukim.team.project.zborleapi.Model.DTO.GameState;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.*;
import finki.ukim.team.project.zborleapi.Model.UserGuessStatus;
import finki.ukim.team.project.zborleapi.Model.DailyWord;
import finki.ukim.team.project.zborleapi.Model.UserGuess;
import finki.ukim.team.project.zborleapi.Model.Word;
import finki.ukim.team.project.zborleapi.Repository.DailyWordRepository;
import finki.ukim.team.project.zborleapi.Repository.UserGuessRepository;
import finki.ukim.team.project.zborleapi.Repository.UserRepository;
import finki.ukim.team.project.zborleapi.Repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordleGameService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private DailyWordRepository dailyWordRepository;

    @Autowired
    private UserGuessRepository userGuessRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public void setDailyWordAndFinalizeGames() {
        // Finalize incomplete games
        finalizeIncompleteGames();

        // Assign new word of the day
        assignNewWord();
    }

    private void finalizeIncompleteGames() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DailyWord yesterdayWord = dailyWordRepository.findByDate(yesterday).orElse(null);

        if (yesterdayWord != null) {
            // Logic to handle unfinished games
            // You can log the unfinished games or handle them as needed
        }
    }

    private DailyWord assignNewWord() {
        // Select a random word from the Word repository
        List<Word> words = wordRepository.findAll();
        if (words.isEmpty()) {
            throw new IllegalStateException("No words available to assign as the word of the day.");
        }
        Word newWord = words.get((int) (Math.random() * words.size()));

        // Save the new word as the word of the day
        DailyWord dailyWord = new DailyWord();
        dailyWord.setDate(LocalDate.now());
        dailyWord.setWord(newWord.getWord());
        dailyWordRepository.save(dailyWord);

        return dailyWord;
    }

    @Transactional
    public GameFeedback checkWord(String guess) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
                new IllegalStateException("User not found")
        );

        // Get the word of the day
        DailyWord dailyWord = dailyWordRepository.findByDate(LocalDate.now())
                .orElseGet(this::assignNewWord);

        // Ensure the guessed word exists in the database
        if (!wordRepository.existsByWordIgnoreCase(guess)) {
            return new GameFeedback(Collections.emptyList(), "Your guessed word does not exist", Collections.emptyList());
        }

        String target = dailyWord.getWord().toLowerCase(); // Ensure target is lowercase
        guess = guess.toLowerCase(); // Ensure guess is lowercase

        List<UserGuessResponse> currentGuessResponses = new ArrayList<>();
        String message;

        // Use a map to count occurrences of each character
        Map<Character, Integer> letterCount = new HashMap<>();
        for (char c : target.toCharArray()) {
            letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
        }

        int correctCount = 0;

        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            char targetChar = target.charAt(i);
            Answer answer;

            if (guessChar == targetChar) {
                answer = Answer.CORRECT;
                correctCount++;
                letterCount.put(guessChar, letterCount.get(guessChar) - 1);
            } else if (letterCount.containsKey(guessChar) && letterCount.get(guessChar) > 0) {
                answer = Answer.PARTIALLY_CORRECT;
                letterCount.put(guessChar, letterCount.get(guessChar) - 1);
            } else {
                answer = Answer.NOT_CORRECT;
            }
            currentGuessResponses.add(new UserGuessResponse(String.valueOf(guessChar), answer, i));
        }

        // Save the current guess to the database
        UserGuess userGuess = UserGuess.builder()
                .user(user)
                .guess(guess)
                .status(currentGuessResponses.stream()
                        .map(r -> UserGuessStatus.builder()
                                .letter(r.getLetter())
                                .answer(r.getAnswer())
                                .character_order(r.getCharacter_order())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        userGuessRepository.save(userGuess);

        if (correctCount == target.length()) {
            message = "You found it!";
            // Additional logic for handling when the user wins
        } else {
            if (userGuessRepository.findByUser(user).size() >= 6) {
                message = "Game over! The correct word was " + target;
                // Additional logic to mark the game as finished after 6 attempts
            } else {
                message = "Try again!";
            }
        }

        // Retrieve all previous guesses
        List<GuessResult> allGuesses = userGuessRepository.findByUser(user).stream()
                .map(g -> new GuessResult(g.getGuess(),
                        g.getStatus().stream()
                                .map(s -> new UserGuessResponse(s.getLetter(), s.getAnswer(), s.getCharacter_order()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return new GameFeedback(currentGuessResponses, message, allGuesses);
    }

    @Transactional
    public GameState startOrContinueGame(User user) {
        // Ensure that a DailyWord exists for today
        DailyWord dailyWord = dailyWordRepository.findByDate(LocalDate.now())
                .orElseGet(this::assignNewWord);

        // Set the start and end of today as LocalDateTime
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        // Retrieve all previous guesses for today's word by the user
        List<UserGuess> userGuesses = userGuessRepository.findByUserAndCreatedAtBetween(user, startOfDay, endOfDay);

        // Create a list of GuessResult to hold each guess with its status
        List<GuessResult> guessResults = userGuesses.stream()
                .map(g -> new GuessResult(
                        g.getGuess(),
                        g.getStatus().stream()
                                .map(status -> new UserGuessResponse(
                                        status.getLetter(),
                                        status.getAnswer(),
                                        status.getCharacter_order()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());

        // Return a GameState object that includes the DailyWord and all guesses
        return GameState.builder()
                .dailyWord(dailyWord)
                .guesses(guessResults)
                .build();
    }

    public UserStatistics getUserStatistics(User user) {
        // Retrieve all guesses made by the user
        List<UserGuess> userGuesses = userGuessRepository.findByUser(user);

        // Calculate games played (based on unique dates of guesses)
        long gamesPlayed = userGuesses.stream()
                .map(UserGuess::getCreatedAt)
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .count();

        // Calculate games won (based on the correct guess being made)
        long gamesWon = userGuesses.stream()
                .filter(userGuess -> userGuess.getStatus().stream()
                        .allMatch(status -> status.getAnswer() == Answer.CORRECT))
                .map(UserGuess::getCreatedAt)
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .count();

        // Calculate win percentage
        double winPercentage = gamesPlayed > 0 ? (gamesWon / (double) gamesPlayed) * 100 : 0;

        // Calculate average attempts (only for won games)
        double averageAttempts = userGuesses.stream()
                .collect(Collectors.groupingBy(
                        guess -> guess.getCreatedAt().toLocalDate(),
                        Collectors.counting()))
                .values().stream()
                .mapToLong(Long::longValue)
                .average().orElse(0);

        // Return UserStatistics including user details
        return UserStatistics.builder()
                .first_name(user.getFirstname())
                .last_name(user.getLastname())
                .email(user.getEmail())
                .games_played(gamesPlayed)
                .games_won(gamesWon)
                .win_percentage(winPercentage)
                .average_attempts(averageAttempts)
                .build();
    }
    public List<UserStatisticsResponse> getAllUsersStatistics() {
        List<User> users = userRepository.findAll();
        List<UserStatisticsResponse> statisticsList = new ArrayList<>();

        for (User user : users) {
            UserStatistics stats = getUserStatistics(user);
            UserStatisticsResponse response = UserStatisticsResponse.builder()
                    .first_name(user.getFirstname())
                    .last_name(user.getLastname())
                    .email(user.getEmail())
                    .games_played(stats.getGames_played())
                    .games_won(stats.getGames_won())
                    .win_percentage(stats.getWin_percentage())
                    .average_attempts(stats.getAverage_attempts())
                    .build();

            statisticsList.add(response);
        }

        return statisticsList;
    }
}