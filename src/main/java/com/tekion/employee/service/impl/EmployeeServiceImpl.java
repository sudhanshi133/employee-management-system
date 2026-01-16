package com.tekion.employee.service.impl;

import com.tekion.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public interface EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public EmployeeEntity create(EmployeeEntity entity) {
        entity.setCreatedTime(Instant.now());
        return repository.save(entity);
    }

    @Override
    public List<EmployeeEntity> getNextPage(Instant cursor, int limit) {
        return repository.fetchNextPage(cursor, limit);
    }

    @Override
    public boolean delete(String id) {
        return repository.deleteById(id);
    }
}
