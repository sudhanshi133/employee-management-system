# Employee API - Quick Reference

## Base URL
```
http://localhost:8080/employee
```

---

## 🔥 All API Endpoints

### 1. CREATE Employee
```bash
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com", "city": "NYC", "designation": "Engineer", "salary": 75000.0}'
```

### 2. GET All Employees
```bash
curl http://localhost:8080/employee/getAll
```

### 3. GET Employee by ID
```bash
curl http://localhost:8080/employee/get/{id}
```

### 4. UPDATE Employee
```bash
curl -X PUT http://localhost:8080/employee/update/{id} \
  -H "Content-Type: application/json" \
  -d '{"salary": 85000.0}'
```

### 5. DELETE Employee
```bash
curl -X DELETE http://localhost:8080/employee/{id}
```

### 6. SEARCH by Email & Designation
```bash
curl "http://localhost:8080/employee/search?email=john@example.com&designation=Engineer"
```

### 7. GET by Date Range
```bash
curl "http://localhost:8080/employee/range?startTime=2026-01-01T00:00:00Z&endTime=2026-01-16T23:59:59Z"
```

### 8. GET Latest N Employees
```bash
curl "http://localhost:8080/employee/latest?limit=10"
```

### 9. INCREMENT Salary (Atomic)
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/{id}?amount=5000"
```

### 10. PAGINATE (Cursor-based)
```bash
# First page
curl "http://localhost:8080/employee/page?limit=5"

# Next page
curl "http://localhost:8080/employee/page?cursor=2026-01-16T10:21:56.066Z&limit=5"
```

---

## 📋 Summary Table

| Method | Endpoint | Description | Task |
|--------|----------|-------------|------|
| POST | `/employee` | Create employee | Task 5 |
| GET | `/employee/getAll` | Get all employees | Task 5 |
| GET | `/employee/get/{id}` | Get by ID | Task 5 |
| PUT | `/employee/update/{id}` | Update employee | Task 5 |
| DELETE | `/employee/{id}` | Delete employee | Task 5 |
| GET | `/employee/search` | Search by email & designation | Task 6 |
| GET | `/employee/range` | Get by date range (sorted) | Task 7 |
| GET | `/employee/latest` | Get latest N (sorted) | Task 8 |
| PATCH | `/employee/increment-salary/{id}` | Atomic salary increment | Task 10 |
| GET | `/employee/page` | Cursor-based pagination | Bonus |

---

## 🧪 Quick Test Script

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/employee"

# Create
ID=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","city":"NYC","designation":"Engineer","salary":70000}' \
  | jq -r '.id')

echo "Created employee with ID: $ID"

# Get by ID
curl -s $BASE_URL/get/$ID | jq '.'

# Update
curl -s -X PUT $BASE_URL/update/$ID \
  -H "Content-Type: application/json" \
  -d '{"salary": 80000}' | jq '.'

# Increment salary
curl -s -X PATCH "$BASE_URL/increment-salary/$ID?amount=5000"

# Verify
curl -s $BASE_URL/get/$ID | jq '.salary'

# Search
curl -s "$BASE_URL/search?email=test@example.com&designation=Engineer" | jq '.'

# Latest
curl -s "$BASE_URL/latest?limit=5" | jq '.'

# Delete
curl -s -X DELETE $BASE_URL/$ID

echo "Test completed!"
```

Save as `test_api.sh`, make executable with `chmod +x test_api.sh`, and run with `./test_api.sh`

---

**Note:** Replace `{id}` with actual employee ID from responses. Use `jq` for pretty JSON output.

