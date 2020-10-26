package edu.exam.udp.datasource.repositories;

import edu.exam.udp.datasource.models.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    Optional<Employee> findByLoginAndPassword(String login, String password);

    Optional<Employee> getEmployeeById(Integer id);
}

