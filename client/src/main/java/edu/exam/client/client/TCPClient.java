package edu.exam.client.client;

import edu.exam.client.auth.Authenticator;
import edu.exam.client.configuration.TCPGateway;
import edu.exam.common.MessageException;
import edu.exam.common.models.Error;
import edu.exam.common.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import static edu.exam.client.utils.Constants.*;

@Service
@DependsOn("client")
public class TCPClient implements ITCPClient {

    private final TCPGateway gateway;
    private final Authenticator authenticator;

    @Autowired
    public TCPClient(TCPGateway gateway,
                     Authenticator authenticator) {
        this.gateway = gateway;
        this.authenticator = authenticator;
    }

    public String login(String username, String password) throws MessageException {
        Credentials credentials = new Credentials()
                .setUsername(username)
                .setPassword(password);
        Message<Credentials> message = new Message<Credentials>()
                .setData(credentials);

        Message<Content> response = gateway.login(message);
        Content content = response.getData();

        throwExceptionIfErrorResponse(content);

        if (content instanceof User) {
            User user = (User) content;
            username = user.toString();
            authenticator.authorize(username, content.getMessage());
            return String.format(WELCOME_MESSAGE, username);
        }

        throw new MessageException(UNEXPECTED_RESPONSE);
    }

    @Override
    public String logout() throws MessageException {
        if (!authenticator.isAuthorized())
            throw new MessageException(LOGOUT_UNAUTHORIZED_MESSAGE);

        Message<Content> message = new Message<>();
        Message<Content> response = gateway.logout(authenticator.getToken(), message);
        Content content = response.getData();

        throwExceptionIfErrorResponse(content);

        String username = authenticator.getUsername();
        authenticator.deauthorize();
        return String.format(BYE_MESSAGE, username);
    }

    @Override
    public String getById(int id) throws MessageException {
        if (!authenticator.isAuthorized())
            throw new MessageException(COMMON_UNAUTHORIZED_MESSAGE);

        Message<Content> message = new Message<>()
                .setData(new Content().setMessage(String.valueOf(id)));
        Message<Content> response = gateway.getById(authenticator.getToken(), message);
        Content content = response.getData();

        throwExceptionIfErrorResponse(content);

        if (content instanceof Histories) {
            Histories histories = (Histories) content;
            if (histories.isEmpty())
                return String.format(NO_HISTORIES_FOUND_BY_ID_TEMPLATE, id);
            return toOutputTable(histories);
        }

        throw new MessageException(UNEXPECTED_RESPONSE);

    }

    @Override
    public String getByName(String name) throws MessageException {
        if (!authenticator.isAuthorized())
            throw new MessageException(COMMON_UNAUTHORIZED_MESSAGE);

        Message<Content> message = new Message<>()
                .setData(new Content().setMessage(name));
        Message<Content> response = gateway.getByName(authenticator.getToken(), message);
        Content content = response.getData();

        throwExceptionIfErrorResponse(content);

        if (response.getData() instanceof Histories) {
            Histories histories = (Histories) content;
            if (histories.isEmpty())
                return String.format(NO_HISTORIES_FOUND_BY_NAME_TEMPLATE, name);
            return toOutputTable(histories);
        }

        throw new MessageException(UNEXPECTED_RESPONSE);
    }

    public static void throwExceptionIfErrorResponse(Content content) throws MessageException {
        if (content instanceof Error)
            throw new MessageException(((Error) content).getErrorMessage());
    }

    private String toOutputTable(Histories histories) {
        StringBuilder builder = new StringBuilder();
        String headers = String.format(
                TABLE_HEADERS_TEMPLATE,
                EMPLOYEE_COLUMN_NAME,
                POSITION_COLUMN_NAME,
                MANAGER_COLUMN_NAME,
                HIRE_COLUMN_NAME,
                DISMISS_COLUMN_NAME
        );
        builder.append(headers);
        histories.getHistories().forEach(history -> builder.append(
                String.format(TABLE_HEADERS_TEMPLATE,
                        history.getEmployee(),
                        history.getPosition(),
                        history.getManager(),
                        history.getHire(),
                        history.getDismiss() == null ?
                                MISSING_VALUE : history.getDismiss())
        ));

        return builder.toString();
    }
}

