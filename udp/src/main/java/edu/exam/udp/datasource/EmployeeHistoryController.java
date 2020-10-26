package edu.exam.udp.datasource;

import edu.exam.common.models.Content;
import edu.exam.common.models.Histories;
import edu.exam.common.models.History;
import edu.exam.common.models.User;
import edu.exam.udp.datasource.models.Employee;
import edu.exam.udp.datasource.models.EmployeeHistory;
import edu.exam.udp.datasource.repositories.EmployeeHistoryRepository;
import edu.exam.udp.datasource.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static edu.exam.udp.utils.Constants.DATE_TIME_FORMAT;

@Controller
public class EmployeeHistoryController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;

    @Autowired
    public EmployeeHistoryController(EmployeeRepository employeeRepository,
                                     EmployeeHistoryRepository employeeHistoryRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeHistoryRepository = employeeHistoryRepository;
    }


    public Content getEmployeeHistoryByCode(Integer code) {
        return mapEmployeeHistoriesToContent(
                new ArrayList<>(employeeHistoryRepository
                        .getAllByEmployeeId(code)));
    }

    public Content getEmployeeHistoryByLastName(String lastName) {
        return mapEmployeeHistoriesToContent(
                new ArrayList<>(employeeHistoryRepository
                        .findAllByEmployeeLastName(lastName)));
    }


    private Histories mapEmployeeHistoriesToContent(List<EmployeeHistory> employeeHistories) {
        Histories histories = new Histories();
        employeeHistories.forEach(h -> histories.add(mapEmployeeHistoryToHistory(h)));
        return histories;
    }

    private History mapEmployeeHistoryToHistory(EmployeeHistory employeeHistory) {
        LocalDate dismiss = employeeHistory.getDismiss();
        return new History()
                .setEmployee(mapEmployeeToUser(
                        employeeHistory
                                .getEmployee()))
                .setPosition(employeeHistory.getPosition())
                .setManager(mapEmployeeToUser(
                        employeeRepository
                                .getEmployeeById(employeeHistory
                                        .getManagerId())
                                .orElse(null)))
                .setHire(employeeHistory.getHire().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .setDismiss(dismiss == null ? null : dismiss.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    }

    private User mapEmployeeToUser(Employee employee) {
        return employee != null ? new User(employee.getName(), employee.getLastName()) : null;
    }
}
