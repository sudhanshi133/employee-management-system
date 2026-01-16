package com.tekion.employee.repository.impl;

import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.repository.EmployeeCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<EmployeeEntity> fetchNextPage(Instant cursor, int limit) {
        // construct your paginated query here
        return mongoTemplate.find(query, EmployeeEntity.class);
    }
}

