package edu.exam.udp.server;

import edu.exam.common.models.Content;
import edu.exam.common.models.Credentials;
import edu.exam.common.models.Error;
import edu.exam.common.models.Message;
import edu.exam.udp.datasource.EmployeeController;
import edu.exam.udp.datasource.EmployeeHistoryController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;

import static edu.exam.udp.utils.Constants.*;

@Slf4j
@MessageEndpoint
class UDPServer {
    private final EmployeeController employeeController;
    private final EmployeeHistoryController employeeHistoryController;

    @Autowired
    public UDPServer(EmployeeController employeeController,
                     EmployeeHistoryController employeeHistoryController) {
        this.employeeController = employeeController;
        this.employeeHistoryController = employeeHistoryController;
    }

    @ServiceActivator(inputChannel = VALIDATE_CREDENTIALS_CHANNEL, outputChannel = UDP_OUT_CHANNEL)
    public Message<Content> validateCredentials(@Payload Message<Content> message) {
        if (message.getData() instanceof Credentials) {
            Credentials credentials = (Credentials) message.getData();
            Content content = employeeController.validateEmployee(credentials);
            message.setData(content);
        } else message.setData(new Error().setErrorMessage(UNEXPECTED_REQUEST_FORMAT));

        return message;
    }

    @ServiceActivator(inputChannel = GET_HISTORY_BY_EMPLOYEE_ID_CHANNEL, outputChannel = UDP_OUT_CHANNEL)
    public Message<Content> getHistoryByEmployeeId(@Payload Message<Content> message) {
        try {
            Integer id = Integer.parseInt(message.getData().getMessage());
            message.setData(employeeHistoryController.getEmployeeHistoryByCode(id));
        } catch (NumberFormatException exception) {
            message.setData(new Error().setErrorMessage(UNEXPECTED_REQUEST_FORMAT));
        }

        return message;
    }

    @ServiceActivator(inputChannel = GET_HISTORY_BY_EMPLOYEE_LAST_NAME_CHANNEL, outputChannel = UDP_OUT_CHANNEL)
    public Message<Content> getHistoryByEmployeeLastName(@Payload Message<Content> message) {
        String lastName = message.getData().getMessage();
        message.setData(employeeHistoryController.getEmployeeHistoryByLastName(lastName));
        return message;
    }

}
