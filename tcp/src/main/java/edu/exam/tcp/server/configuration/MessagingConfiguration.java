package edu.exam.tcp.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.MessageChannel;

import static edu.exam.tcp.server.utils.Constants.*;
import static org.springframework.integration.scheduling.PollerMetadata.DEFAULT_POLLER;

@Configuration
public class MessagingConfiguration {

    @Bean(name = AUTH_IN_CHANNEL)
    public MessageChannel authIn() {
        return new ExecutorChannel(executor());
    }

    @Bean(name = AUTH_OUT_CHANNEL)
    public MessageChannel authOut() {
        return new ExecutorChannel(executor());
    }

    @Bean(name = UDP_IN_CHANNEL)
    public MessageChannel udpIn() {
        return new QueueChannel();
    }

    @Bean(name = UDP_OUT_CHANNEL)
    public MessageChannel udpOut() {
        return new QueueChannel();
    }

    @Bean(name = TCP_IN_CHANNEL)
    public MessageChannel tcpIn() {
        return new QueueChannel();
    }

    @Bean(name = TCP_OUT_CHANNEL)
    public MessageChannel tcpOut() {
        return new QueueChannel();
    }

    @Bean(name = LOGIN_IN_CHANNEL)
    public MessageChannel loginIn() {
        return new QueueChannel();
    }

    @Bean(name = LOGIN_OUT_CHANNEL)
    public MessageChannel loginOut() {
        return new QueueChannel();
    }

    @Bean(name = LOGOUT_IN_CHANNEL)
    public MessageChannel logoutIn() {
        return new ExecutorChannel(executor());
    }

    @Bean(name = DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(POLLING_RATE);
    }

    @Bean
    public AsyncTaskExecutor executor() {
        return new SimpleAsyncTaskExecutor();
    }
}
