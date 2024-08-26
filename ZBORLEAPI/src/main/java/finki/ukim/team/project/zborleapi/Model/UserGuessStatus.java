package finki.ukim.team.project.zborleapi.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGuessStatus {

    private String letter;
    @Enumerated(EnumType.STRING)
    private Answer answer;
    private int characterOrder;
}