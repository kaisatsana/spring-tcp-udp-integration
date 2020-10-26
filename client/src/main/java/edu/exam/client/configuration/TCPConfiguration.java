package edu.exam.client.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exam.client.auth.Authenticator;
import edu.exam.common.configuration.CommonConfiguration;
import edu.exam.common.models.Content;
import edu.exam.common.models.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpOutboundGatewaySpec;
import org.springframework.integration.ip.tcp.connection.MessageConvertingTcpMessageMapper;
import org.springframework.integration.ip.tcp.connection.TcpConnectionFailedEvent;
import org.springframework.integration.ip.tcp.serializer.MapJsonSerializer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.MessageChannel;

import static edu.exam.client.utils.Constants.DEFAULT_CHANNEL;

@Configuration
@Import(CommonConfiguration.class)
public class TCPConfiguration {

    @Bean
    public TcpOutboundGatewaySpec client(@Value("${tcp.server.host}") String host,
                                         @Value("${tcp.server.port}") int port,
                                         MapJsonSerializer serializer,
                                         MessageConvertingTcpMessageMapper mapper) {
        return Tcp.outboundGateway(Tcp.netClient(host, port)
                .serializer(serializer)
                .deserializer(serializer)
                .mapper(mapper));
    }

    @Bean
    public IntegrationFlow outbound(TcpOutboundGatewaySpec client) {
        return IntegrationFlows
                .from(TCPGateway.class)
                .channel(DEFAULT_CHANNEL)
                .transform(new ObjectToJsonTransformer())
                .handle(client)
                .bridge()
                .transform(map -> new ObjectMapper().convertValue(map,
                        new TypeReference<Message<Content>>() {
                        }))
                .get();
    }

    @Bean(name = DEFAULT_CHANNEL)
    public MessageChannel defaultChannel() {
        return new DirectChannel();
    }

    @Bean
    public Authenticator authenticator() {
        return Authenticator.builder()
                .isAuthorized(false)
                .build();
    }
}
