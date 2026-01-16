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

    @Override
    public List<EmployeeEntity> getAll(){
        return repository.findAll();
    }

    @Override
    public EmployeeEntity getById(String id){
        return repository.findById(id).orElse(null);
    }

    @Override
    public EmployeeEntity update(String id, EmployeeEntity entity) {
        EmployeeEntity existingEntity = repository.findById(id).orElse(null);
        if (existingEntity == null) {
            return null;
        }
        if (entity.getName() != null) {
            existingEntity.setName(entity.getName());
        }
        if (entity.getEmail() != null) {
            existingEntity.setEmail(entity.getEmail());
        }
        if (entity.getCity() != null) {
            existingEntity.setCity(entity.getCity());
        }
        if (entity.getDesignation() != null) {
            existingEntity.setDesignation(entity.getDesignation());
        }
        if (entity.getSalary() != null) {
            existingEntity.setSalary(entity.getSalary());
        }

        existingEntity.setModifiedAt(Instant.now());
        return repository.save(existingEntity);
    }

    @Override
    public List<EmployeeEntity> findByCreatedAtRange(Instant startTime, Instant endTime) {
        return repository.findByCreatedAtBetweenSorted(startTime, endTime);
    }

    @Override
    public List<EmployeeEntity> findLatestEmployees(int limit) {
        return repository.findLatestEmployees(limit);
    }

    @Override
    public boolean incrementSalary(String id, Double amount) {
        return repository.incrementSalary(id, amount);
    }
}
