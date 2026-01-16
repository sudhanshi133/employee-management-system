package com.tekion.employee.repository;

import com.tekion.employee.models.EmployeeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<EmployeeEntity, String>, EmployeeCustomRepository {
}
