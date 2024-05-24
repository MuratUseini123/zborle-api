package finki.ukim.team.project.zborleapi.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class UserWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    private int numberOfAttempts;

    private boolean userCurrentWord;

    private boolean completed;

    private boolean won;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private LocalDate gameDate;

    @ElementCollection
    private List<String> guesses = new ArrayList<>();

    public void setAttempts(int attempts) {
        this.numberOfAttempts = attempts;
    }

    public int getAttempts() {
        return this.numberOfAttempts;
    }
}
