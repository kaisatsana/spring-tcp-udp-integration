package edu.exam.tcp.server;

import edu.exam.common.utils.Headers;
import edu.exam.common.models.Error;
import edu.exam.common.models.*;
import edu.exam.common.utils.Options;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

import static edu.exam.tcp.server.utils.Constants.*;

@Slf4j
@MessageEndpoint
public class TCPServer {

    private final AuthController authController;

    public static final int TOKEN_LENGTH = 10;
    private final Map<String, String> sessions;

    @Autowired
    public TCPServer(Map<String, String> sessions, AuthController authController) {
        this.sessions = sessions;
        this.authController = authController;
    }

    @ServiceActivator(inputChannel = LOGIN_IN_CHANNEL, outputChannel = UDP_OUT_CHANNEL)
    public Message<Content> loginIn(@Header(IpHeaders.CONNECTION_ID) String connectionId,
                                    @Payload Message<Content> message) {
        Meta meta = new Meta()
                .setOption(Options.LOGIN)
                .setConnectionId(connectionId);
        message.setMeta(meta);
        return message;
    }


    @ServiceActivator(inputChannel = LOGIN_OUT_CHANNEL, outputChannel = TCP_OUT_CHANNEL)
    public Message<Content> loginOut(@Payload Message<Content> message) {
        Content content = message.getData();
        if (content instanceof User) {
            String token = authController.authenticate(message.getMeta().getConnectionId());
            message.getData().setMessage(token);
        } else if (content instanceof Error) {
            Error error = (Error) content;
            String errorMessage = ACCESS_DENIED_PREFIX + error.getErrorMessage();
            error.setErrorMessage(errorMessage);
        } else message.setData(new Error().setErrorMessage(UNEXPECTED_UDP_RESPONSE));

        return message;
    }

    @ServiceActivator(inputChannel = LOGOUT_IN_CHANNEL, outputChannel = TCP_OUT_CHANNEL)
    public Message<Content> logout(@Header(IpHeaders.CONNECTION_ID) String connectionId,
                                   @Header(Headers.AUTH_TOKEN) String token,
                                   @Payload Message<Content> message) {
        if (authController.isAuthorized(connectionId, token)) {
            authController.deauthorize(connectionId);
        } else message.setData(new Error().setErrorMessage(ACCESS_DENIED_PREFIX + INVALID_AUTH_TOKEN));

        return message;
    }

    @Transformer(inputChannel = AUTH_IN_CHANNEL, outputChannel = AUTH_OUT_CHANNEL)
    public Message<Content> auth(@Header(IpHeaders.CONNECTION_ID) String connectionId,
                                 @Header(Headers.AUTH_TOKEN) String token,
                                 @Header(Headers.OPTION) String option,
                                 @Payload Message<Content> message) {

        if (!authController.isAuthorized(connectionId, token))
            message.setData(new Error().setErrorMessage(ACCESS_DENIED_PREFIX + INVALID_AUTH_TOKEN));

        Meta meta = new Meta()
                .setConnectionId(connectionId)
                .setOption(option);

        message.setMeta(meta);
        return message;
    }
}
