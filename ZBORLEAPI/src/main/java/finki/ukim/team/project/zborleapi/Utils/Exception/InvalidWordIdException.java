package finki.ukim.team.project.zborleapi.Utils.Exception;

public class InvalidWordIdException extends RuntimeException{
    public InvalidWordIdException() {
        super("Invalid word ID.");
    }

    public InvalidWordIdException(Integer wordId) {
        super("Invalid word ID: " + wordId);
    }

}
