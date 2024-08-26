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
public class GuessResult {
    private String guess;
    private List<UserGuessResponse> status;
}