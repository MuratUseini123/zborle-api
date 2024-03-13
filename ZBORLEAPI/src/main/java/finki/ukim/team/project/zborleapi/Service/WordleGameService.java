package finki.ukim.team.project.zborleapi.Service;
import finki.ukim.team.project.zborleapi.Model.Color;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.GameFeedback;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserGuessResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                userGuessResponse.add(new UserGuessResponse(String.valueOf(guessChar), Color.GREEN, i));
            }
        } else {
            for (int i = 0; i < guess.length(); i++) {
                char guessChar = guess.charAt(i);
                char targetChar = target.charAt(i);
                Color color;
                if (guessChar == targetChar) {
                    color = Color.GREEN;
                    usedIndices.add(i);
                } else if (target.contains(String.valueOf(guessChar))) {
                    color = Color.YELLOW;
                } else {
                    color = Color.GREY;
                }
                userGuessResponse.add(new UserGuessResponse(String.valueOf(guessChar), color,i));
            }
            message = "Give it one more try!";
        }

        return new GameFeedback(userGuessResponse, message);
    }
}