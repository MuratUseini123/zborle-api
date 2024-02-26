package finki.ukim.team.project.zborleapi.Model.DTO.Request;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    private String current_password;
    private String new_password;
    private String confirmation_password;
}