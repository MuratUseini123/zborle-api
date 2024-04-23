package finki.ukim.team.project.zborleapi.Model;

import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class UserWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    private int numberOfAttempts;

    private boolean userCurrentWord;
}