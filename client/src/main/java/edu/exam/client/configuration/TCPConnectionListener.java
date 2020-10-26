package edu.exam.client.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.event.IpIntegrationEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionExceptionEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionFailedEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TCPConnectionListener implements ApplicationListener<IpIntegrationEvent> {

    @Override
    public void onApplicationEvent(@NonNull IpIntegrationEvent tcpConnectionEvent) {
        if (tcpConnectionEvent instanceof TcpConnectionExceptionEvent) {
            log.info("Unable to obtain TCP connection: {}", ((TcpConnectionExceptionEvent) tcpConnectionEvent).getConnectionId());
        } if (tcpConnectionEvent instanceof TcpConnectionFailedEvent) {
            log.info("Unable to connect to TCP server");
        } if (tcpConnectionEvent instanceof TcpConnectionOpenEvent) {
            log.info("Connected to server: {}" + ((TcpConnectionOpenEvent) tcpConnectionEvent).getConnectionId());
        }
    }
}
