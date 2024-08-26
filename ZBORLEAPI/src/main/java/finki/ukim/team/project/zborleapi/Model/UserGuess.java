package finki.ukim.team.project.zborleapi.Model;

import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGuess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String guess;

    @ElementCollection
    @CollectionTable(name = "user_guess_status", joinColumns = @JoinColumn(name = "user_guess_id"))
    @Column(name = "status")
    private List<UserGuessStatus> status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}