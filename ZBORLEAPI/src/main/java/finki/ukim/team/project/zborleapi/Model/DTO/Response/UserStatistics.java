package finki.ukim.team.project.zborleapi.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatistics {
    private String firstName;
    private String lastName;
    private String email;
    private long gamesPlayed;
    private long gamesWon;
    private double winPercentage;
    private double averageAttempts;
}