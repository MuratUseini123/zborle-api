package finki.ukim.team.project.zborleapi.Utils.WordSeeder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import finki.ukim.team.project.zborleapi.Model.Word;
import finki.ukim.team.project.zborleapi.Service.WordService;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class WordSeeder {

    private final WordService wordService;
    private static final Logger logger = LoggerFactory.getLogger(WordSeeder.class);

    @Value("${DICTIONARY_FILE_PATH}")
    private String dictionary;

    public WordSeeder(WordService wordService) {
        this.wordService = wordService;
    }

    @PostConstruct
    public void seed() {
        if (this.wordService.findAll().isEmpty()) {
            logger.info("Starting word seeding process...");
            try (BufferedReader reader = new BufferedReader(new FileReader(dictionary))) {
                String line;
                List<Word> newWords = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if (word.length() <= 5) {
                            Word newWord = new Word(word);
                            newWords.add(newWord);
                        }
                    }
                }

                if (!newWords.isEmpty()) {
                    this.wordService.saveAll(newWords);
                    logger.info("Successfully seeded {} words into the database.", newWords.size());
                } else {
                    logger.warn("No words were found in the dictionary file.");
                }

            } catch (IOException e) {
                logger.error("Error occurred while seeding words: ", e);
                throw new RuntimeException("Failed to seed words from the dictionary file.", e);
            }
        } else {
            logger.info("Skipping word seeding process. Words already exist in the database.");
        }
    }
}
