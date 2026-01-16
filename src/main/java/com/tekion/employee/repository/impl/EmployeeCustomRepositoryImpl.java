package com.tekion.employee.repository.impl;

import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.repository.EmployeeCustomRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    @Override
    public List<EmployeeEntity> findByCreatedAtBetweenSorted(Instant startTime, Instant endTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").gte(startTime).lte(endTime));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, EmployeeEntity.class);
    }

    @Override
    public List<EmployeeEntity> findLatestEmployees(int limit) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        query.limit(limit);
        return mongoTemplate.find(query, EmployeeEntity.class);
    }

    @Override
    public boolean incrementSalary(String id, Double amount) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().inc("salary", amount);
        update.set("modifiedAt", Instant.now());
        UpdateResult result = mongoTemplate.updateFirst(query, update, EmployeeEntity.class);
        return result.getModifiedCount() > 0;
    }
}

