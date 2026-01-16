package com.tekion.employee.repository;

import com.tekion.employee.models.EmployeeEntity;

import java.time.Instant;
import java.util.List;

public interface EmployeeCustomRepository {

    List<EmployeeEntity> fetchNextPage(Instant cursor, int limit);

    List<EmployeeEntity> findByCreatedAtBetweenSorted(Instant startTime, Instant endTime);

    List<EmployeeEntity> findLatestEmployees(int limit);

    boolean incrementSalary(String id, Double amount);
}
