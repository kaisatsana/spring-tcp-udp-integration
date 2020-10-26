package edu.exam.udp.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.MessageChannel;

import static edu.exam.udp.utils.Constants.*;
import static org.springframework.integration.scheduling.PollerMetadata.DEFAULT_POLLER;

@Configuration
public class MessagingConfiguration {

    @Bean(name = UDP_IN_CHANNEL)
    public MessageChannel udpIn() {
        return new QueueChannel();
    }

    @Bean(name = UDP_OUT_CHANNEL)
    public MessageChannel udpOut() {
        return new QueueChannel();
    }

    @Bean(name = VALIDATE_CREDENTIALS_CHANNEL)
    public MessageChannel validateCredentials() {
        return new ExecutorChannel(executor());
    }

    @Bean(name = GET_HISTORY_BY_EMPLOYEE_ID_CHANNEL)
    public MessageChannel getHistoryByEmployeeId() {
        return new ExecutorChannel(executor());
    }

    @Bean(name = GET_HISTORY_BY_EMPLOYEE_LAST_NAME_CHANNEL)
    public MessageChannel getHistoryByEmployeeLastName() {
        return new ExecutorChannel(executor());
    }

    @Bean(name = DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(POLLING_RATE);
    }

    @Bean(name = MESSAGING_EXECUTOR)
    public AsyncTaskExecutor executor() {
        return new SimpleAsyncTaskExecutor();
    }
}

