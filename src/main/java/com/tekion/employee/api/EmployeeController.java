package com.tekion.employee.api;

import com.tekion.employee.models.EmployeeEntity;
import com.tekion.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public EmployeeEntity create(@RequestBody EmployeeEntity entity) {
        return service.create(entity);
    }

    @GetMapping("/page")
    public List<EmployeeEntity> page(@RequestParam(required = false) Instant cursor, @RequestParam int limit) {
        return service.fetchEntities(cursor, limit);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return service.delete(id);
    }

    @GetMapping("/getAll")
    public List<EmployeeEntity> getAll(){
        return service.getAll();
    }

    @GetMapping("/get/{id}")
    public EmployeeEntity getById(@PathVariable String id){
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    public EmployeeEntity update(@PathVariable String id, @RequestBody EmployeeEntity entity) {
        return service.update(id, entity);
    }
}