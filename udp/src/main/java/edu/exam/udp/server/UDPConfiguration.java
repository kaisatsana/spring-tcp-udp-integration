package edu.exam.udp.server;

import edu.exam.common.models.Message;
import edu.exam.common.utils.Options;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.transformer.ObjectToStringTransformer;

import static edu.exam.udp.utils.Constants.*;

@Slf4j
@Configuration
public class UDPConfiguration {

    @Bean
    public IntegrationFlow udpInbound(@Value("${udp.port}") int port) {
        return IntegrationFlows
                .from(Udp.inboundAdapter(port)
                        .id(EMPLOYEE_ADAPTER_ID))
                .log(LoggingHandler.Level.INFO)
                .channel(UDP_IN_CHANNEL)
                .transform(toJson())
                .transform(toMessage())
                .route(ROUTE_BY_OPTION_EXPRESSION, r -> r
                        .channelMapping(Options.LOGIN, VALIDATE_CREDENTIALS_CHANNEL)
                        .channelMapping(Options.GET_BY_NAME, GET_HISTORY_BY_EMPLOYEE_LAST_NAME_CHANNEL)
                        .channelMapping(Options.GET_BY_ID, GET_HISTORY_BY_EMPLOYEE_ID_CHANNEL))
                .get();
    }

    @Bean
    public IntegrationFlow udpOutbound(@Value("${udp.remote.host}") String host,
                                       @Value("${udp.remote.port}") int port) {
        return IntegrationFlows
                .from(UDP_OUT_CHANNEL)
                .transform(fromMessage())
                .log(LoggingHandler.Level.INFO)
                .handle(Udp.outboundAdapter(host, port)
                        .socketExpression(EMPLOYEE_SOCKET_EXPRESSION))
                .get();
    }

    @Bean
    public ObjectToStringTransformer toJson() {
        return new ObjectToStringTransformer();
    }

    @Bean
    public JsonToObjectTransformer toMessage() {
        return new JsonToObjectTransformer(Message.class);
    }

    @Bean
    public ObjectToJsonTransformer fromMessage() {
        return new ObjectToJsonTransformer();
    }
}
