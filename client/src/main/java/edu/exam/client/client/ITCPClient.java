package edu.exam.client.client;

public interface ITCPClient {
    String login(String username, String password) throws Exception;

    String logout() throws Exception;

    String getById(int id) throws Exception;

    String getByName(String name) throws Exception;
}
