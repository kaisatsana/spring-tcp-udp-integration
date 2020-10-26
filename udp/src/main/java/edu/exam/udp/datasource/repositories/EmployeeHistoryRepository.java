package edu.exam.udp.datasource.repositories;

import edu.exam.udp.datasource.models.EmployeeHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeHistoryRepository extends CrudRepository<EmployeeHistory, Integer> {
    List<EmployeeHistory> getAllByEmployeeId(Integer id);

    List<EmployeeHistory> findAllByEmployeeLastName(String lastName);
}

