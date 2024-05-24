package finki.ukim.team.project.zborleapi.API.Word;

import finki.ukim.team.project.zborleapi.Model.DTO.Request.WordRequest;
import finki.ukim.team.project.zborleapi.Model.Word;
import finki.ukim.team.project.zborleapi.Repository.UserRepository;
import finki.ukim.team.project.zborleapi.Service.WordService;
import finki.ukim.team.project.zborleapi.Service.WordleGameService;
import finki.ukim.team.project.zborleapi.Utils.Exception.InvalidWordIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/words")
@Tag(name = "Word API", description = "API for managing words")
@SecurityRequirement(name = "bearerAuth")
public class WordController {
    private final WordService wordService;

    public WordController(WordService wordService, WordleGameService wordleGameService, UserRepository userRepository) {
        this.wordService = wordService;
    }

    @Operation(summary = "Find Word by ID", description = "Retrieves a word by its ID.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Word> findWordById(@PathVariable Integer id) {
        try {
            Word word = wordService.findby(id);
            return ResponseEntity.ok(word);
        } catch (InvalidWordIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get All Words", description = "Retrieves all words.")
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<Word>> getAllWords() {
        List<Word> words = wordService.findAll();
        return ResponseEntity.ok(words);
    }

    @Operation(summary = "Create Word", description = "Creates a new word.")
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Word> createWord(@RequestBody WordRequest word) {
        Word newWord = wordService.create(word);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWord);
    }

    @Operation(summary = "Update Word", description = "Updates an existing word.")
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

    @Operation(summary = "Delete Word", description = "Deletes a word by its ID.")
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

    @Operation(summary = "Batch Save Words", description = "Saves multiple words at once.")
    @PostMapping("/batch")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Void> saveAllWords(@RequestBody List<Word> words) {
        wordService.saveAll(words);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}