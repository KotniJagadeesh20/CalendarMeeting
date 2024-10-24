package com.example.meetingShedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.meetingShedule.Entity.Employee;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{
  
	
}

