package finki.ukim.team.project.zborleapi.Model.DTO;

import finki.ukim.team.project.zborleapi.Model.DTO.Response.GuessResult;
import finki.ukim.team.project.zborleapi.Model.DailyWord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    private DailyWord dailyWord;
    private List<GuessResult> guesses;
}