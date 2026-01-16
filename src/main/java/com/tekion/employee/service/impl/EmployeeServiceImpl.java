package com.tekion.employee.service.impl;

import com.tekion.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

public interface EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public EmployeeEntity create(EmployeeEntity entity) {
        entity.setCreatedTime(Instant.now());
        return repository.save(entity);
    }

    public List<EmployeeEntity> getNextPage(Instant cursor, int limit) {
        return repository.fetchNextPage(cursor, limit);
    }

    public boolean delete(String id) {
        return repository.deleteById(id);
    }
}
