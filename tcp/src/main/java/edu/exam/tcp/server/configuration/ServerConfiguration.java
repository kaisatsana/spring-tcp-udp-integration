package edu.exam.tcp.server.configuration;

import edu.exam.common.configuration.CommonConfiguration;
import edu.exam.common.models.Content;
import edu.exam.common.models.Error;
import edu.exam.common.models.Message;
import edu.exam.common.utils.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpInboundChannelAdapterSpec;
import org.springframework.integration.ip.dsl.TcpOutboundChannelAdapterSpec;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.integration.ip.tcp.connection.MessageConvertingTcpMessageMapper;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.MapJsonSerializer;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static edu.exam.tcp.server.utils.Constants.*;

@Configuration
@Import(CommonConfiguration.class)
public class ServerConfiguration {

    @Bean
    public TcpNetServerConnectionFactory server(@Value("${tcp.port}") int port,
                                                MapJsonSerializer serializer,
                                                MessageConvertingTcpMessageMapper mapper) {
        TcpNetServerConnectionFactory server = new TcpNetServerConnectionFactory(port);
        server.setSerializer(serializer);
        server.setDeserializer(serializer);
        server.setMapper(mapper);
        return server;
    }

    @Bean
    public TcpInboundChannelAdapterSpec tcpInboundAdapter(TcpNetServerConnectionFactory server) {
        return Tcp.inboundAdapter(server);
    }

    @Bean
    public TcpOutboundChannelAdapterSpec tcpOutboundAdapter(TcpNetServerConnectionFactory server) {
        return Tcp.outboundAdapter(server);
    }


    @Bean
    public IntegrationFlow tcpOutbound(TcpOutboundChannelAdapterSpec tcpOutboundAdapter) {
        return IntegrationFlows.from(TCP_OUT_CHANNEL)
                .enrichHeaders(headers -> headers
                        .headerExpression(
                                IpHeaders.CONNECTION_ID, HEADERS_BY_CONNECTION_ID_EXPRESSION))
                .log(LoggingHandler.Level.INFO)
                .handle(tcpOutboundAdapter)
                .get();
    }

    @Bean
    public IntegrationFlow tcpInbound(TcpInboundChannelAdapterSpec tcpInboundAdapter) {
        return IntegrationFlows
                .from(tcpInboundAdapter)
                .log(LoggingHandler.Level.INFO)
                .transform(toMessage())
                .route(HEADERS_BY_OPTION_EXPRESSION, r -> r
                        .resolutionRequired(false)
                        .channelMapping(Options.LOGIN, LOGIN_IN_CHANNEL)
                        .channelMapping(Options.LOGOUT, LOGOUT_IN_CHANNEL)
                        .defaultOutputChannel(AUTH_IN_CHANNEL))
                .get();
    }

    @Bean
    public IntegrationFlow authBridge() {
        return IntegrationFlows.from(AUTH_OUT_CHANNEL)
                .bridge()
                .<Message<Content>, Boolean>route(message ->
                        message.getData() instanceof Error, s -> s
                        .channelMapping(true, TCP_OUT_CHANNEL)
                        .channelMapping(false, UDP_OUT_CHANNEL))
                .get();
    }

    @Bean
    public IntegrationFlow udpInbound(@Value("${udp.port}") int port) {
        return IntegrationFlows
                .from(Udp.inboundAdapter(port)
                        .id(UDP_ADAPTER_ID))
                .transform(toMessage())
                .route(UDP_ROUTE_BY_OPTION_EXPRESSION, r -> r
                        .resolutionRequired(false)
                        .channelMapping(Options.LOGIN, LOGIN_OUT_CHANNEL)
                        .defaultOutputChannel(TCP_OUT_CHANNEL))
                .get();
    }

    @Bean
    public IntegrationFlow udpOutbound(@Value("${remote.udp.host}") String remoteHost,
                                       @Value("${remote.udp.employee.port}") int employeePort) {
        return IntegrationFlows
                .from(UDP_OUT_CHANNEL)
                .transform(toJson())
                .handle(Udp.outboundAdapter(remoteHost, employeePort)
                        .socketExpression(UDP_SOCKET_EXPRESSION))
                .get();
    }

    @Bean
    public JsonToObjectTransformer toMessage() {
        return new JsonToObjectTransformer(Message.class);
    }

    @Bean
    public ObjectToJsonTransformer toJson() {
        return new ObjectToJsonTransformer();
    }

    @Bean
    public Map<String, String> sessions() {
        return Collections.synchronizedMap(new HashMap<>());
    }
}
