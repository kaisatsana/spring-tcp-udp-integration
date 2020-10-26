package edu.exam.udp.datasource;

import edu.exam.common.models.Content;
import edu.exam.common.models.Credentials;
import edu.exam.common.models.Error;
import edu.exam.common.models.User;
import edu.exam.udp.datasource.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.atomic.AtomicReference;

import static edu.exam.udp.utils.Constants.INVALID_USERNAME_OR_PASSWORD;

@Controller
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Content validateEmployee(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        AtomicReference<Content> content = new AtomicReference<>();
        employeeRepository
                .findByLoginAndPassword(username, password)
                .ifPresentOrElse(e -> content
                                .set(new User()
                                        .setName(e.getName())
                                        .setLastName(e.getLastName())),
                        () -> content
                                .set(new Error()
                                        .setErrorMessage(INVALID_USERNAME_OR_PASSWORD)));
        return content.get();
    }
}