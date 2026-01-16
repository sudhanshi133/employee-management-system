package com.tekion.employee.service;

import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


public class EmployeeService {

    EmployeeEntity create(EmployeeEntity entity);

    List<EmployeeEntity> getNextPage(Instant cursor, int limit);

    boolean delete(String id);
}
