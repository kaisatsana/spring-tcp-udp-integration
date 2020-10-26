package edu.exam.common.configuration;

import edu.exam.common.utils.Headers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ip.tcp.connection.MessageConvertingTcpMessageMapper;
import org.springframework.integration.ip.tcp.serializer.MapJsonSerializer;
import org.springframework.integration.support.converter.MapMessageConverter;

@Configuration
@ComponentScan
public class CommonConfiguration {

    @Bean
    public MessageConvertingTcpMessageMapper mapper() {
        MapMessageConverter converter = new MapMessageConverter();
        converter.setHeaderNames(Headers.OPTION, Headers.AUTH_TOKEN);
        return new MessageConvertingTcpMessageMapper(converter);
    }

    @Bean
    public MapJsonSerializer serializer() {
        return new MapJsonSerializer();
    }
}
