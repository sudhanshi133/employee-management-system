package com.tekion.employee.repository.impl;

import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.repository.EmployeeCustomRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

    private final MongoTemplate mongoTemplate;

    public EmployeeCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<EmployeeEntity> fetchNextPage(Instant cursor, int limit) {
        Query query = new Query();
        if (cursor != null) {
            query.addCriteria(Criteria.where("createdAt").gt(cursor));
        }
        query.limit(limit);
        return mongoTemplate.find(query, EmployeeEntity.class);
    }
}

