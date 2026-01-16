# Employee Management System - Complete API Documentation

**Base URL:** `http://localhost:8080/employee`

---

## 📋 Table of Contents

1. [Task 5 - Basic CRUD APIs](#task-5---basic-crud-apis)
2. [Task 6 - Query by Non-ID Fields](#task-6---query-by-non-id-fields)
3. [Task 7 - Sorted Range Query](#task-7---sorted-range-query)
4. [Task 8 - Limited & Sorted Read](#task-8---limited--sorted-read)
5. [Task 9 - Uniqueness Enforcement](#task-9---uniqueness-enforcement)
6. [Task 10 - Atomic Update](#task-10---atomic-update)
7. [Bonus - Cursor-Based Pagination](#bonus---cursor-based-pagination)

---

## Task 5 - Basic CRUD APIs

### 1. CREATE Employee
**Endpoint:** `POST /employee`  
**Description:** Create a new employee record

```bash
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "city": "New York",
    "designation": "Software Engineer",
    "salary": 75000.0
  }'
```

**Response:**
```json
{
  "id": "696a1144320438259b7ccdff",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "city": "New York",
  "designation": "Software Engineer",
  "salary": 75000.0,
  "createdAt": "2026-01-16T10:21:56.066Z",
  "modifiedAt": null
}
```

---

### 2. GET All Employees
**Endpoint:** `GET /employee/getAll`  
**Description:** Fetch all employees from database

```bash
curl -X GET http://localhost:8080/employee/getAll
```

**With Pretty Print:**
```bash
curl -s http://localhost:8080/employee/getAll | jq '.'
```

---

### 3. GET Employee by ID
**Endpoint:** `GET /employee/get/{id}`  
**Description:** Fetch a specific employee by ID

```bash
curl -X GET http://localhost:8080/employee/get/696a1144320438259b7ccdff
```

---

### 4. UPDATE Employee
**Endpoint:** `PUT /employee/update/{id}`  
**Description:** Update employee (supports partial updates)

**Full Update:**
```bash
curl -X PUT http://localhost:8080/employee/update/696a1144320438259b7ccdff \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "email": "john.updated@example.com",
    "city": "Los Angeles",
    "designation": "Senior Software Engineer",
    "salary": 95000.0
  }'
```

**Partial Update (Only Salary):**
```bash
curl -X PUT http://localhost:8080/employee/update/696a1144320438259b7ccdff \
  -H "Content-Type: application/json" \
  -d '{"salary": 85000.0}'
```

---

### 5. DELETE Employee
**Endpoint:** `DELETE /employee/{id}`  
**Description:** Delete an employee by ID

```bash
curl -X DELETE http://localhost:8080/employee/696a1144320438259b7ccdff
```

**Response:** `true`

---

## Task 6 - Query by Non-ID Fields

### Search by Email and Designation (LogN Query)
**Endpoint:** `GET /employee/search`  
**Description:** Query employees by email and designation (indexed fields, O(log N) complexity)

```bash
curl -X GET "http://localhost:8080/employee/search?email=john.doe@example.com&designation=Software%20Engineer"
```

**Example with URL encoding:**
```bash
curl -X GET "http://localhost:8080/employee/search?email=jane.smith@example.com&designation=Senior%20Software%20Engineer"
```

**Performance:** O(log N) due to indexes on `email` and `designation` fields

---

## Task 7 - Sorted Range Query

### Get Employees by Created Date Range
**Endpoint:** `GET /employee/range`  
**Description:** Fetch employees created between two timestamps, sorted by createdAt DESC

```bash
curl -X GET "http://localhost:8080/employee/range?startTime=2026-01-01T00:00:00Z&endTime=2026-01-16T23:59:59Z"
```

**Last 7 Days:**
```bash
curl -X GET "http://localhost:8080/employee/range?startTime=2026-01-09T00:00:00Z&endTime=2026-01-16T23:59:59Z"
```

**All of 2026:**
```bash
curl -X GET "http://localhost:8080/employee/range?startTime=2026-01-01T00:00:00Z&endTime=2026-12-31T23:59:59Z"
```

**Performance:** Sorting happens at MongoDB level, not in Java

---

## Task 8 - Limited & Sorted Read

### Get Latest N Employees
**Endpoint:** `GET /employee/latest`  
**Description:** Fetch the N most recently created employees, sorted by createdAt DESC

```bash
curl -X GET "http://localhost:8080/employee/latest?limit=10"
```

**Latest 5:**
```bash
curl -X GET "http://localhost:8080/employee/latest?limit=5"
```

**Latest 20:**
```bash
curl -X GET "http://localhost:8080/employee/latest?limit=20"
```

**Performance:** Limit applied at database level, no in-memory pagination

---

## Task 9 - Uniqueness Enforcement

### Test Unique Email Constraint
**Description:** Email field has unique index - duplicate emails will be rejected

**Create First Employee:**
```bash
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User 1",
    "email": "unique@test.com",
    "city": "NYC",
    "designation": "Developer",
    "salary": 70000.0
  }'
```

**Try Duplicate Email (Should Fail):**
```bash
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User 2",
    "email": "unique@test.com",
    "city": "LA",
    "designation": "Engineer",
    "salary": 80000.0
  }'
```

**Expected:** MongoDB will throw duplicate key error

**Implementation:** `@Indexed(unique = true)` on email field

---

## Task 10 - Atomic Update

### Increment Salary (Atomic Operation)
**Endpoint:** `PATCH /employee/increment-salary/{id}`  
**Description:** Atomically increment salary using MongoDB's $inc operator (thread-safe)

**Increase by $5000:**
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=5000"
```

**Decrease by $2000:**
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=-2000"
```

**Give $1000 Raise:**
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=1000"
```

**Performance:** Atomic operation, safe under concurrent requests

---

## Bonus - Cursor-Based Pagination

### Paginated Fetch (No Skip)
**Endpoint:** `GET /employee/page`  
**Description:** Efficient cursor-based pagination (no skip operation)

**First Page:**
```bash
curl -X GET "http://localhost:8080/employee/page?limit=5"
```

**Next Page (with cursor from last employee's createdAt):**
```bash
curl -X GET "http://localhost:8080/employee/page?cursor=2026-01-16T10:21:56.066Z&limit=5"
```

**Performance:** O(log N) - uses cursor instead of skip for better performance

---

## 🧪 Testing Workflow

### Complete Test Sequence

**1. Create Multiple Employees:**
```bash
# Employee 1
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Johnson", "email": "alice@company.com", "city": "San Francisco", "designation": "Software Engineer", "salary": 90000.0}'

# Employee 2
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name": "Bob Smith", "email": "bob@company.com", "city": "New York", "designation": "Senior Software Engineer", "salary": 120000.0}'

# Employee 3
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name": "Carol White", "email": "carol@company.com", "city": "Austin", "designation": "Software Engineer", "salary": 85000.0}'

# Employee 4
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name": "David Brown", "email": "david@company.com", "city": "Seattle", "designation": "Tech Lead", "salary": 140000.0}'
```

**2. Get All Employees:**
```bash
curl -s http://localhost:8080/employee/getAll | jq '.'
```

**3. Search by Email and Designation:**
```bash
curl -s "http://localhost:8080/employee/search?email=alice@company.com&designation=Software%20Engineer" | jq '.'
```

**4. Get Latest 3 Employees:**
```bash
curl -s "http://localhost:8080/employee/latest?limit=3" | jq '.'
```

**5. Increment Salary:**
```bash
# Get employee ID from previous response, then:
curl -X PATCH "http://localhost:8080/employee/increment-salary/YOUR_EMPLOYEE_ID?amount=5000"
```

**6. Verify Update:**
```bash
curl -s http://localhost:8080/employee/get/YOUR_EMPLOYEE_ID | jq '.'
```

**7. Test Pagination:**
```bash
# First page
curl -s "http://localhost:8080/employee/page?limit=2" | jq '.'

# Next page (use createdAt from last employee)
curl -s "http://localhost:8080/employee/page?cursor=2026-01-16T10:30:00.000Z&limit=2" | jq '.'
```

---

## 📊 Database Indexes

The following indexes are configured for optimal performance:

1. **Email Index:** `@Indexed(unique = true)` - Ensures uniqueness and fast lookups
2. **Designation Index:** `@Indexed` - Fast queries by designation
3. **CreatedAt Index:** `@Indexed` - Efficient sorting and range queries
4. **Compound Index:** `email + designation` - Optimizes search endpoint

---

## 🔧 Technical Implementation Details

### Repository Methods
- `findByEmailAndDesignation()` - Derived query method (O(log N))
- `findByCreatedAtBetweenSorted()` - Custom query with sorting
- `findLatestEmployees()` - Custom query with limit
- `incrementSalary()` - Atomic update using MongoTemplate
- `fetchNextPage()` - Cursor-based pagination

### Performance Characteristics
- **Create:** O(log N) - due to index maintenance
- **Read by ID:** O(1) - direct lookup
- **Update:** O(log N) - index updates
- **Delete:** O(log N) - index cleanup
- **Search:** O(log N) - indexed query
- **Range Query:** O(log N + K) - where K is result size
- **Pagination:** O(log N) - cursor-based, no skip

---

## 🚀 Quick Start

**1. Start MongoDB:**
```bash
mongod --dbpath /path/to/data
```

**2. Run Application:**
```bash
mvn spring-boot:run
```

**3. Test Health:**
```bash
curl http://localhost:8080/employee/getAll
```

---

## 📝 Notes

- All timestamps are in ISO 8601 format (UTC)
- Salary is stored as Double
- Email must be unique across all employees
- Partial updates are supported (only send fields to update)
- Atomic operations prevent race conditions
- Cursor-based pagination is more efficient than offset-based for large datasets

---

**Generated:** 2026-01-16
**Version:** 1.0
**Author:** Employee Management System

