# Employee Management System - Complete API Reference with CURL Commands

Base URL: `http://localhost:8080/employee`

---

## 1. CREATE Employee
**Endpoint:** `POST /employee`  
**Description:** Create a new employee record

### CURL Command:
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

### Response:
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

## 2. GET All Employees
**Endpoint:** `GET /employee/getAll`  
**Description:** Fetch all employees from the database

### CURL Command:
```bash
curl -X GET http://localhost:8080/employee/getAll
```

### Pretty Print with jq:
```bash
curl -s http://localhost:8080/employee/getAll | jq '.'
```

---

## 3. GET Employee by ID
**Endpoint:** `GET /employee/get/{id}`  
**Description:** Fetch a specific employee by their ID

### CURL Command:
```bash
curl -X GET http://localhost:8080/employee/get/696a1144320438259b7ccdff
```

### Response:
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

## 4. UPDATE Employee
**Endpoint:** `PUT /employee/update/{id}`  
**Description:** Update an existing employee (partial update supported)

### CURL Command (Update All Fields):
```bash
curl -X PUT http://localhost:8080/employee/update/696a1144320438259b7ccdff \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated Doe",
    "email": "john.updated@example.com",
    "city": "Los Angeles",
    "designation": "Senior Software Engineer",
    "salary": 95000.0
  }'
```

### CURL Command (Partial Update - Only Salary):
```bash
curl -X PUT http://localhost:8080/employee/update/696a1144320438259b7ccdff \
  -H "Content-Type: application/json" \
  -d '{
    "salary": 85000.0
  }'
```

---

## 5. DELETE Employee
**Endpoint:** `DELETE /employee/{id}`  
**Description:** Delete an employee by ID

### CURL Command:
```bash
curl -X DELETE http://localhost:8080/employee/696a1144320438259b7ccdff
```

### Response:
```json
true
```

---

## 6. PAGINATED Fetch (Cursor-Based)
**Endpoint:** `GET /employee/page`  
**Description:** Fetch employees with cursor-based pagination (no skip, efficient)

### CURL Command (First Page):
```bash
curl -X GET "http://localhost:8080/employee/page?limit=5"
```

### CURL Command (Next Page with Cursor):
```bash
curl -X GET "http://localhost:8080/employee/page?cursor=2026-01-16T10:21:56.066Z&limit=5"
```

### Parameters:
- `cursor` (optional): Timestamp from the last employee's `createdAt` field
- `limit` (required): Number of records to fetch

---

## 7. GET Employees by Created Date Range (Sorted)
**Endpoint:** `GET /employee/range`  
**Description:** Fetch employees created between two timestamps, sorted by createdAt DESC

### CURL Command:
```bash
curl -X GET "http://localhost:8080/employee/range?startTime=2026-01-01T00:00:00Z&endTime=2026-01-16T23:59:59Z"
```

### Parameters:
- `startTime` (required): ISO 8601 timestamp (e.g., `2026-01-01T00:00:00Z`)
- `endTime` (required): ISO 8601 timestamp (e.g., `2026-01-16T23:59:59Z`)

### Example with Today's Date:
```bash
curl -X GET "http://localhost:8080/employee/range?startTime=$(date -u -v-7d +%Y-%m-%dT%H:%M:%SZ)&endTime=$(date -u +%Y-%m-%dT%H:%M:%SZ)"
```

---

## 8. GET Latest Employees (Limited & Sorted)
**Endpoint:** `GET /employee/latest`  
**Description:** Fetch the N most recently created employees, sorted by createdAt DESC

### CURL Command (Get Latest 10):
```bash
curl -X GET "http://localhost:8080/employee/latest?limit=10"
```

### CURL Command (Get Latest 5):
```bash
curl -X GET "http://localhost:8080/employee/latest?limit=5"
```

---

## 9. INCREMENT Salary (Atomic Operation)
**Endpoint:** `PATCH /employee/increment-salary/{id}`  
**Description:** Atomically increment an employee's salary using MongoDB's $inc operator

### CURL Command (Increment by $5000):
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=5000"
```

### CURL Command (Decrement by $2000):
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=-2000"
```

### Response:
```json
true
```

---

## Quick Test Script

Save this as `test_all_apis.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/employee"

echo "1. Creating Employee..."
EMPLOYEE_ID=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","city":"TestCity","designation":"Tester","salary":50000}' \
  | jq -r '.id')
echo "Created Employee ID: $EMPLOYEE_ID"

echo -e "\n2. Getting All Employees..."
curl -s $BASE_URL/getAll | jq '.'

echo -e "\n3. Getting Employee by ID..."
curl -s $BASE_URL/get/$EMPLOYEE_ID | jq '.'

echo -e "\n4. Updating Employee..."
curl -s -X PUT $BASE_URL/update/$EMPLOYEE_ID \
  -H "Content-Type: application/json" \
  -d '{"salary":60000}' | jq '.'

echo -e "\n5. Incrementing Salary..."
curl -s -X PATCH "$BASE_URL/increment-salary/$EMPLOYEE_ID?amount=5000"

echo -e "\n6. Getting Latest 5 Employees..."
curl -s "$BASE_URL/latest?limit=5" | jq '.'

echo -e "\n7. Getting Employees in Date Range..."
curl -s "$BASE_URL/range?startTime=2026-01-01T00:00:00Z&endTime=2026-12-31T23:59:59Z" | jq '.'

echo -e "\n8. Paginated Fetch (First 3)..."
curl -s "$BASE_URL/page?limit=3" | jq '.'

echo -e "\n9. Deleting Employee..."
curl -s -X DELETE $BASE_URL/$EMPLOYEE_ID

echo -e "\nAll tests completed!"
```

Make it executable: `chmod +x test_all_apis.sh`

---

## Summary Table

| # | Method | Endpoint | Description |
|---|--------|----------|-------------|
| 1 | POST | `/employee` | Create new employee |
| 2 | GET | `/employee/getAll` | Get all employees |
| 3 | GET | `/employee/get/{id}` | Get employee by ID |
| 4 | PUT | `/employee/update/{id}` | Update employee |
| 5 | DELETE | `/employee/{id}` | Delete employee |
| 6 | GET | `/employee/page` | Cursor-based pagination |
| 7 | GET | `/employee/range` | Get by date range (sorted) |
| 8 | GET | `/employee/latest` | Get latest N employees |
| 9 | PATCH | `/employee/increment-salary/{id}` | Atomic salary increment |


