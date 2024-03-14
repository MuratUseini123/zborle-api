package finki.ukim.team.project.zborleapi.Service;
import finki.ukim.team.project.zborleapi.Model.Answer;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.GameFeedback;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserGuessResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordleGameService {

    public GameFeedback checkWord(String guess, String target) {
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