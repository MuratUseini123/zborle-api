package finki.ukim.team.project.zborleapi.API.Word;

import finki.ukim.team.project.zborleapi.Model.DTO.Request.UserGuessRequest;
import finki.ukim.team.project.zborleapi.Model.DTO.Request.WordRequest;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.GameFeedback;
import finki.ukim.team.project.zborleapi.Model.Word;
import finki.ukim.team.project.zborleapi.Service.WordService;
import finki.ukim.team.project.zborleapi.Service.WordleGameService;
import finki.ukim.team.project.zborleapi.Utils.Exception.InvalidWordIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/words")
public class WordController {
    private final WordService wordService;
    private final WordleGameService wordleGameService;


    public WordController(WordService wordService, WordleGameService wordleGameService) {
        this.wordService = wordService;
        this.wordleGameService = wordleGameService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Word> findWordById(@PathVariable Integer id) {
        try {
            Word word = wordService.findby(id);
            return ResponseEntity.ok(word);
        } catch (InvalidWordIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Word>> getAllWords() {
        List<Word> words = wordService.findAll();
        return ResponseEntity.ok(words);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Word> createWord(@RequestBody WordRequest word) {
        Word newWord = wordService.create(word);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWord);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Word> updateWord(@PathVariable Integer id, @RequestBody WordRequest newWord) {
        try {
            Word updatedWord = wordService.update(id, newWord);
            return ResponseEntity.ok(updatedWord);
        } catch (InvalidWordIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Word> deleteWord(@PathVariable Integer id) {
        try {
            Word deletedWord = wordService.delete(id);
            return ResponseEntity.ok(deletedWord);
        } catch (InvalidWordIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/batch")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Void> saveAllWords(@RequestBody List<Word> words) {
        wordService.saveAll(words);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/check-word")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<GameFeedback> checkWord(@RequestBody UserGuessRequest guess) {
        String targetWord = "LEMON";
        GameFeedback response = wordleGameService.checkWord(guess.getGuess(), targetWord);
        return ResponseEntity.ok(response);
    }
}
