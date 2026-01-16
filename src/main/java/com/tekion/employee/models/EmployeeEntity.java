package com.tekion.employee.models;

import java.time.Instant;

@Document(collection = "employees")
public class EmployeeEntity {

    @Id
    private String id;
    private String name;
    private Instant createdTime;
}
