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

@Component
public class WordSeeder {

    private final WordService wordService;

    @Value("${DICTIONARY_FILE_PATH}")
    private String dictionary;

    public WordSeeder(WordService wordService) {
        this.wordService = wordService;
    }

    @PostConstruct
    public void seed(){
        try {
            if(this.wordService.findAll().isEmpty()){
                BufferedReader reader = new BufferedReader(new FileReader(dictionary));
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
                this.wordService.saveAll(newWords);
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
