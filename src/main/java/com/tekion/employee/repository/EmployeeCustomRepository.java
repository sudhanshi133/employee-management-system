package com.tekion.employee.repository;

import com.tekion.employee.models.EmployeeEntity;

import java.time.Instant;
import java.util.List;

public class EmployeeCustomRepository {

    List<EmployeeEntity> fetchNextPage(Instant cursor, int limit);
}
