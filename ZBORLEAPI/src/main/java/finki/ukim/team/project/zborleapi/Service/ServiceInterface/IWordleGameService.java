package finki.ukim.team.project.zborleapi.Service.ServiceInterface;

import finki.ukim.team.project.zborleapi.Model.DTO.Response.UserGuessResponse;

import java.util.List;

public interface IWordleGameService {
    List<UserGuessResponse> checkWord(String guess, String target);
}

