package edu.exam.client.configuration;

import edu.exam.common.utils.Headers;
import edu.exam.common.utils.Options;
import edu.exam.common.models.Content;
import edu.exam.common.models.Credentials;
import edu.exam.common.models.Message;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static edu.exam.client.utils.Constants.DEFAULT_CHANNEL;

@Component
@MessagingGateway(defaultRequestChannel = DEFAULT_CHANNEL)
public interface TCPGateway {

    @Gateway(headers = @GatewayHeader(name = Headers.OPTION, value = Options.LOGIN))
    Message<Content> login(@Payload Message<Credentials> message);

    @Gateway(headers = @GatewayHeader(name = Headers.OPTION, value = Options.LOGOUT))
    Message<Content> logout(@Header(Headers.AUTH_TOKEN) String token,
                            @Payload Message<Content> message);

    @Gateway(headers = @GatewayHeader(name = Headers.OPTION, value = Options.GET_BY_ID))
    Message<Content> getById(@Header(Headers.AUTH_TOKEN) String token,
                             @Payload Message<Content> message);

    @Gateway(headers = @GatewayHeader(name = Headers.OPTION, value = Options.GET_BY_NAME))
    Message<Content> getByName(@Header(Headers.AUTH_TOKEN) String token,
                               @Payload Message<Content> message);

}
