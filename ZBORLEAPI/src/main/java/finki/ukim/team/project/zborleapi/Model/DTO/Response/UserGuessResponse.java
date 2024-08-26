package finki.ukim.team.project.zborleapi.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import finki.ukim.team.project.zborleapi.Model.Answer;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGuessResponse {
    private String letter;
    private Answer answer;
    private int character_order;
}
