package finki.ukim.team.project.zborleapi.Service;
import finki.ukim.team.project.zborleapi.Model.Answer;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.GameFeedback;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserGuessResponse;
import finki.ukim.team.project.zborleapi.Repository.UserWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import finki.ukim.team.project.zborleapi.Model.UserWord;
import finki.ukim.team.project.zborleapi.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordleGameService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserWordRepository userWordRepository;

    public GameFeedback checkWord(String guess) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByEmail(username).orElse(null);
        UserWord userWord = userWordRepository.findUserWordByUserAndUserCurrentWordIsTrue(user);
        userWord.getWord().getWord();
        String target = user != null ? userWord.getWord().getWord() : null;

        List<UserGuessResponse> userGuessResponse = new ArrayList<>();
        List<Integer> usedIndices = new ArrayList<>();
        String message;

        if (guess.equals(target)) {
            message = "You found it!";
            for (int i = 0; i < guess.length(); i++) {
                char guessChar = guess.charAt(i);
                userGuessResponse.add(new UserGuessResponse(String.valueOf(guessChar), Answer.CORRECT, i));
            }
        } else {
            for (int i = 0; i < guess.length(); i++) {
                char guessChar = guess.charAt(i);
                char targetChar = target.charAt(i);
                Answer answer;
                if (guessChar == targetChar) {
                    answer = Answer.CORRECT;
                    usedIndices.add(i);
                } else if (target.contains(String.valueOf(guessChar))) {
                    answer = Answer.PARTIALLY_CORRECT;
                } else {
                    answer = Answer.NOT_CORRECT;
                }
                userGuessResponse.add(new UserGuessResponse(String.valueOf(guessChar), answer,i));
            }
            message = "Give it one more try!";
        }
        return new GameFeedback(userGuessResponse, message);
    }
}