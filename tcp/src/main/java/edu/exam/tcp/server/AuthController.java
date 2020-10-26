package edu.exam.tcp.server;

import edu.exam.common.utils.Headers;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@MessageEndpoint
public class AuthController {
    public static final int TOKEN_LENGTH = 10;

    private final Map<String, String> sessions;

    @Autowired
    public AuthController(Map<String, String> sessions) {
        this.sessions = sessions;
    }

    public String authenticate(String connectionId) {
        String token = RandomStringUtils.randomAscii(TOKEN_LENGTH);
        sessions.put(connectionId, token);
        return token;
    }

    public boolean isAuthorized(@Header(IpHeaders.CONNECTION_ID) String connectionId,
                                @Header(Headers.AUTH_TOKEN) String token) {
        return sessions.get(connectionId) != null && sessions.get(connectionId).equals(token);
    }

    public void deauthorize(String connectionId) {
        if (sessions.get(connectionId) != null)
            sessions.remove(connectionId);
    }
}
