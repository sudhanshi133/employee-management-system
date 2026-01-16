package com.tekion.employee.service.impl;

import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.repository.EmployeeRepository;
import com.tekion.employee.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmployeeEntity create(EmployeeEntity entity) {
        entity.setCreatedAt(Instant.now());
        return repository.save(entity);
    }

    @Override
    public List<EmployeeEntity> fetchEntities(Instant cursor, int limit) {
        return repository.fetchNextPage(cursor, limit);
    }

    @Override
    public boolean delete(String id) {
        repository.deleteById(id);
        return true;
    }
}
