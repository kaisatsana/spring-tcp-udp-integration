package edu.exam.client.shell;

import edu.exam.client.client.TCPClient;
import edu.exam.common.MessageException;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.integration.MessageTimeoutException;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.UncheckedIOException;
import java.net.ConnectException;

import static edu.exam.client.utils.Constants.TCP_SERVER_ERROR;

@ShellComponent
public class ShellRunner {
    private final TCPClient client;

    public ShellRunner(TCPClient client) {
        this.client = client;
    }

    public static void printError(String error) {
        System.out.println(AnsiOutput.toString(AnsiColor.RED, error, AnsiColor.DEFAULT));
    }

    @ShellMethod(key = "login", value = "Log in to Employee History System")
    public String login(@ShellOption({"username", "u"}) String username,
                        @ShellOption({"password", "p"}) String password) {
        try {
            String response = client.login(username, password);
            return AnsiOutput.toString(AnsiColor.BLUE, response, AnsiColor.DEFAULT);
        } catch (MessageException e) {
            return AnsiOutput.toString(AnsiColor.RED, e.getMessage(), AnsiColor.DEFAULT);
        } catch (MessagingException | UncheckedIOException e) {
            return AnsiOutput.toString(AnsiColor.RED, TCP_SERVER_ERROR, AnsiColor.DEFAULT);
        }
    }

    @ShellMethod(key = "logout", value = "Logout from Employee History System")
    public String logout() {
        try {
            String response = client.logout();
            return AnsiOutput.toString(AnsiColor.BLUE, response);
        } catch (MessageException e) {
            return AnsiOutput.toString(AnsiColor.RED, e.getMessage(), AnsiColor.DEFAULT);
        } catch (MessagingException | UncheckedIOException e) {
            return AnsiOutput.toString(AnsiColor.RED, TCP_SERVER_ERROR, AnsiColor.DEFAULT);
        }
    }

    @ShellMethod(key = "by-id", value = "Get Employee History by Employee ID")
    public String getById(@ShellOption("id") Integer id) {
        try {
            return client.getById(id);
        } catch (MessageException e) {
            return AnsiOutput.toString(AnsiColor.RED, e.getMessage(), AnsiColor.DEFAULT);
        } catch (MessagingException | UncheckedIOException e) {
            return AnsiOutput.toString(AnsiColor.RED, TCP_SERVER_ERROR, AnsiColor.DEFAULT);
        }
    }

    @ShellMethod(key = "by-name", value = "Get Employee History by Employee ID")
    public String getByName(@ShellOption({"name", "n"}) String name) {
        try {
            return client.getByName(name);
        } catch (MessageException e) {
            return AnsiOutput.toString(AnsiColor.RED, e.getMessage(), AnsiColor.DEFAULT);
        } catch (MessagingException | UncheckedIOException e) {
            return AnsiOutput.toString(AnsiColor.RED, TCP_SERVER_ERROR, AnsiColor.DEFAULT);
        }
    }
}
