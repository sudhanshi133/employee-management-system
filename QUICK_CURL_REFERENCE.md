# Quick CURL Reference - Employee Management System

## 🚀 Quick Copy-Paste Commands

### 1️⃣ CREATE Employee
```bash
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Johnson","email":"alice@example.com","city":"Seattle","designation":"Developer","salary":80000}'
```

---

### 2️⃣ GET All Employees
```bash
curl -X GET http://localhost:8080/employee/getAll
```

**With Pretty Print:**
```bash
curl -s http://localhost:8080/employee/getAll | jq '.'
```

---

### 3️⃣ GET Employee by ID
```bash
# Replace {id} with actual employee ID
curl -X GET http://localhost:8080/employee/get/696a1144320438259b7ccdff
```

---

### 4️⃣ UPDATE Employee
**Full Update:**
```bash
curl -X PUT http://localhost:8080/employee/update/696a1144320438259b7ccdff \
  -H "Content-Type: application/json" \
  -d '{"name":"John Updated","email":"john.new@example.com","city":"Boston","designation":"Senior Dev","salary":90000}'
```

**Partial Update (Only Salary):**
```bash
curl -X PUT http://localhost:8080/employee/update/696a1144320438259b7ccdff \
  -H "Content-Type: application/json" \
  -d '{"salary":95000}'
```

---

### 5️⃣ DELETE Employee
```bash
curl -X DELETE http://localhost:8080/employee/696a1144320438259b7ccdff
```

---

### 6️⃣ PAGINATED Fetch (Cursor-Based)
**First Page:**
```bash
curl -X GET "http://localhost:8080/employee/page?limit=5"
```

**Next Page (with cursor):**
```bash
curl -X GET "http://localhost:8080/employee/page?cursor=2026-01-16T10:21:56.066Z&limit=5"
```

---

### 7️⃣ GET by Date Range (Sorted)
**Last 7 Days:**
```bash
curl -X GET "http://localhost:8080/employee/range?startTime=2026-01-09T00:00:00Z&endTime=2026-01-16T23:59:59Z"
```

**All of 2026:**
```bash
curl -X GET "http://localhost:8080/employee/range?startTime=2026-01-01T00:00:00Z&endTime=2026-12-31T23:59:59Z"
```

**Today Only:**
```bash
curl -X GET "http://localhost:8080/employee/range?startTime=$(date -u +%Y-%m-%d)T00:00:00Z&endTime=$(date -u +%Y-%m-%d)T23:59:59Z"
```

---

### 8️⃣ GET Latest N Employees
**Latest 10:**
```bash
curl -X GET "http://localhost:8080/employee/latest?limit=10"
```

**Latest 5:**
```bash
curl -X GET "http://localhost:8080/employee/latest?limit=5"
```

---

### 9️⃣ INCREMENT Salary (Atomic)
**Increase by $5000:**
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=5000"
```

**Decrease by $2000:**
```bash
curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=-2000"
```

---

## 📊 Useful One-Liners

### Count Total Employees
```bash
curl -s http://localhost:8080/employee/getAll | jq '. | length'
```

### Get Only Names and Emails
```bash
curl -s http://localhost:8080/employee/getAll | jq '.[] | {name, email}'
```

### Get Employees with Salary > $90,000
```bash
curl -s http://localhost:8080/employee/getAll | jq '.[] | select(.salary > 90000)'
```

### Get Average Salary
```bash
curl -s http://localhost:8080/employee/getAll | jq '[.[] | .salary] | add / length'
```

### Get Highest Paid Employee
```bash
curl -s http://localhost:8080/employee/getAll | jq 'max_by(.salary)'
```

### Get Employees by City
```bash
curl -s http://localhost:8080/employee/getAll | jq '.[] | select(.city == "Seattle")'
```

---

## 🔥 Bulk Operations

### Create 5 Employees at Once
```bash
for i in {1..5}; do
  curl -X POST http://localhost:8080/employee \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"Employee $i\",\"email\":\"emp$i@example.com\",\"city\":\"City$i\",\"designation\":\"Role$i\",\"salary\":$((50000 + i * 5000))}"
  echo ""
done
```

### Give Everyone a $1000 Raise
```bash
curl -s http://localhost:8080/employee/getAll | jq -r '.[].id' | while read id; do
  curl -X PATCH "http://localhost:8080/employee/increment-salary/$id?amount=1000"
  echo " - Raised salary for $id"
done
```

---

## 🧪 Testing Scenarios

### Test Unique Email Constraint
```bash
# Create first employee
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name":"Test1","email":"duplicate@test.com","city":"NYC","designation":"Dev","salary":70000}'

# Try to create duplicate (should fail)
curl -X POST http://localhost:8080/employee \
  -H "Content-Type: application/json" \
  -d '{"name":"Test2","email":"duplicate@test.com","city":"LA","designation":"Dev","salary":80000}'
```

### Test Pagination
```bash
# Get first page
FIRST_PAGE=$(curl -s "http://localhost:8080/employee/page?limit=3")
echo "$FIRST_PAGE" | jq '.'

# Extract cursor from last employee
CURSOR=$(echo "$FIRST_PAGE" | jq -r '.[-1].createdAt')

# Get next page
curl -s "http://localhost:8080/employee/page?cursor=$CURSOR&limit=3" | jq '.'
```

### Test Atomic Increment (Concurrent Safety)
```bash
# Run 10 concurrent increments of $100 each
for i in {1..10}; do
  curl -X PATCH "http://localhost:8080/employee/increment-salary/696a1144320438259b7ccdff?amount=100" &
done
wait

# Verify final salary (should be original + $1000)
curl -s http://localhost:8080/employee/get/696a1144320438259b7ccdff | jq '.salary'
```

---

## 📝 Notes

- Replace `696a1144320438259b7ccdff` with actual employee IDs from your database
- All timestamps must be in ISO 8601 format: `YYYY-MM-DDTHH:MM:SSZ`
- Use `jq` for JSON formatting (install with `brew install jq` on macOS)
- The atomic increment operation is thread-safe and uses MongoDB's `$inc` operator
- Pagination uses cursor-based approach (no skip) for better performance


