package finki.ukim.team.project.zborleapi.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameFeedback {
    private List<UserGuessResponse> current_response;  // This will hold the current guess with statuses
    private String message;
    private List<GuessResult> guesses;  // This will hold all previous guesses with their statuses
}