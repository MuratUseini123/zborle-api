package finki.ukim.team.project.zborleapi.API.Word;

import finki.ukim.team.project.zborleapi.Model.AuthModels.User;
import finki.ukim.team.project.zborleapi.Model.DTO.Request.UserGuessRequest;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.GameFeedback;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserStatistics;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserStatisticsResponse;
import finki.ukim.team.project.zborleapi.Model.DailyWord;
import finki.ukim.team.project.zborleapi.Repository.UserRepository;
import finki.ukim.team.project.zborleapi.Service.WordleGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@Tag(name = "Game API", description = "API for managing games")
@SecurityRequirement(name = "bearerAuth")
public class GameController {

    private final WordleGameService wordleGameService;
    private final UserRepository userRepository;

    public GameController(WordleGameService wordleGameService, UserRepository userRepository) {
        this.wordleGameService = wordleGameService;
        this.userRepository = userRepository;
    }

    @PostMapping("/start-game")
    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
    @Operation(summary = "Start a new game or continue the current game", description = "Starts a new game for the user or continues the current game if already started")
    public ResponseEntity<DailyWord> startGame() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        DailyWord dailyWord = wordleGameService.startOrContinueGame();
        return ResponseEntity.ok(dailyWord);
    }

    @PostMapping("/check-word")
    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
    @Operation(summary = "Check a guessed word", description = "Checks the guessed word and returns feedback")
    public ResponseEntity<GameFeedback> checkWord(@RequestBody UserGuessRequest guess) {
        GameFeedback response = wordleGameService.checkWord(guess.getGuess());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-statistics")
    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
    @Operation(summary = "Get user statistics", description = "Gets the overall statistics for the user")
    public ResponseEntity<UserStatistics> getUserStatistics() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        UserStatistics userStatistics = wordleGameService.getUserStatistics(user);
        return ResponseEntity.ok(userStatistics);
    }

    @GetMapping("/all-user-statistics")
   // @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = "Get statistics for all users", description = "Gets the overall statistics for all users")
    public ResponseEntity<List<UserStatisticsResponse>> getAllUserStatistics() {
        List<UserStatisticsResponse> statistics = wordleGameService.getAllUsersStatistics();
        return ResponseEntity.ok(statistics);
    }
}