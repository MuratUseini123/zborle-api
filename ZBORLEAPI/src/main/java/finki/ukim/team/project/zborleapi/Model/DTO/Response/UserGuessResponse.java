package finki.ukim.team.project.zborleapi.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import finki.ukim.team.project.zborleapi.Model.Color;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGuessResponse {
    private String letter;
    private Color color;
    int character_order;
}
