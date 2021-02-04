package ominext.timetracking.service;

public interface ILoginService {

    String token(String username, String password);

    boolean isCorrect(String username, String password);
}
