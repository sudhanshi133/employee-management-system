package com.tekion.employee.service;

import com.tekion.employee.models.EmployeeEntity;

import java.time.Instant;
import java.util.List;

public interface EmployeeService {

    EmployeeEntity create(EmployeeEntity entity);

    List<EmployeeEntity> fetchEntities(Instant cursor, int limit);

    boolean delete(String id);

    List<EmployeeEntity> getAll();

    EmployeeEntity getById(String id);

    EmployeeEntity update(String id, EmployeeEntity entity);

    List<EmployeeEntity> findByCreatedAtRange(Instant startTime, Instant endTime);

    List<EmployeeEntity> findLatestEmployees(int limit);

    boolean incrementSalary(String id, Double amount);
}
