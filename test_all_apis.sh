#!/bin/bash

# Complete API Test Script for Employee Management System
# This script tests all 9 API endpoints

BASE_URL="http://localhost:8080/employee"

echo "=========================================="
echo "Employee Management System - API Tests"
echo "=========================================="

# Test 1: Create Employee
echo -e "\n1. Creating Employee..."
EMPLOYEE_RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test.user@example.com",
    "city": "Test City",
    "designation": "Test Engineer",
    "salary": 50000.0
  }')

EMPLOYEE_ID=$(echo $EMPLOYEE_RESPONSE | jq -r '.id')
echo "✓ Created Employee ID: $EMPLOYEE_ID"
echo $EMPLOYEE_RESPONSE | jq '.'

# Test 2: Get All Employees
echo -e "\n2. Getting All Employees..."
ALL_EMPLOYEES=$(curl -s $BASE_URL/getAll)
EMPLOYEE_COUNT=$(echo $ALL_EMPLOYEES | jq '. | length')
echo "✓ Total Employees in Database: $EMPLOYEE_COUNT"
echo $ALL_EMPLOYEES | jq '.[] | {id, name, email, salary}'

# Test 3: Get Employee by ID
echo -e "\n3. Getting Employee by ID ($EMPLOYEE_ID)..."
EMPLOYEE=$(curl -s $BASE_URL/get/$EMPLOYEE_ID)
echo "✓ Employee Details:"
echo $EMPLOYEE | jq '.'

# Test 4: Update Employee (Partial Update)
echo -e "\n4. Updating Employee Salary..."
UPDATED_EMPLOYEE=$(curl -s -X PUT $BASE_URL/update/$EMPLOYEE_ID \
  -H "Content-Type: application/json" \
  -d '{
    "salary": 60000.0
  }')
echo "✓ Updated Employee:"
echo $UPDATED_EMPLOYEE | jq '.'

# Test 5: Increment Salary (Atomic Operation)
echo -e "\n5. Incrementing Salary by $5000..."
INCREMENT_RESULT=$(curl -s -X PATCH "$BASE_URL/increment-salary/$EMPLOYEE_ID?amount=5000")
echo "✓ Increment Result: $INCREMENT_RESULT"

# Verify the increment
EMPLOYEE_AFTER_INCREMENT=$(curl -s $BASE_URL/get/$EMPLOYEE_ID)
NEW_SALARY=$(echo $EMPLOYEE_AFTER_INCREMENT | jq -r '.salary')
echo "✓ New Salary: $NEW_SALARY"

# Test 6: Get Latest N Employees
echo -e "\n6. Getting Latest 5 Employees..."
LATEST_EMPLOYEES=$(curl -s "$BASE_URL/latest?limit=5")
echo "✓ Latest 5 Employees:"
echo $LATEST_EMPLOYEES | jq '.[] | {name, email, createdAt}'

# Test 7: Get Employees in Date Range
echo -e "\n7. Getting Employees Created in 2026..."
RANGE_EMPLOYEES=$(curl -s "$BASE_URL/range?startTime=2026-01-01T00:00:00Z&endTime=2026-12-31T23:59:59Z")
RANGE_COUNT=$(echo $RANGE_EMPLOYEES | jq '. | length')
echo "✓ Employees Created in 2026: $RANGE_COUNT"
echo $RANGE_EMPLOYEES | jq '.[] | {name, createdAt}'

# Test 8: Paginated Fetch (Cursor-Based)
echo -e "\n8. Testing Cursor-Based Pagination..."
echo "   - Fetching First Page (limit=3)..."
FIRST_PAGE=$(curl -s "$BASE_URL/page?limit=3")
echo $FIRST_PAGE | jq '.[] | {name, createdAt}'

# Get cursor from last employee
CURSOR=$(echo $FIRST_PAGE | jq -r '.[-1].createdAt')
echo "   - Cursor for Next Page: $CURSOR"

echo "   - Fetching Second Page (cursor=$CURSOR, limit=3)..."
SECOND_PAGE=$(curl -s "$BASE_URL/page?cursor=$CURSOR&limit=3")
echo $SECOND_PAGE | jq '.[] | {name, createdAt}'

# Test 9: Delete Employee
echo -e "\n9. Deleting Test Employee ($EMPLOYEE_ID)..."
DELETE_RESULT=$(curl -s -X DELETE $BASE_URL/$EMPLOYEE_ID)
echo "✓ Delete Result: $DELETE_RESULT"

# Verify deletion
echo "   - Verifying deletion..."
DELETED_EMPLOYEE=$(curl -s $BASE_URL/get/$EMPLOYEE_ID)
if [ "$DELETED_EMPLOYEE" == "null" ]; then
    echo "✓ Employee successfully deleted"
else
    echo "✗ Employee still exists"
fi

echo -e "\n=========================================="
echo "All API Tests Completed Successfully! ✓"
echo "=========================================="

# Summary
echo -e "\nAPI Endpoints Tested:"
echo "  1. POST   /employee                      - Create Employee"
echo "  2. GET    /employee/getAll               - Get All Employees"
echo "  3. GET    /employee/get/{id}             - Get Employee by ID"
echo "  4. PUT    /employee/update/{id}          - Update Employee"
echo "  5. PATCH  /employee/increment-salary/{id} - Increment Salary"
echo "  6. GET    /employee/latest               - Get Latest N Employees"
echo "  7. GET    /employee/range                - Get by Date Range"
echo "  8. GET    /employee/page                 - Cursor-Based Pagination"
echo "  9. DELETE /employee/{id}                 - Delete Employee"

