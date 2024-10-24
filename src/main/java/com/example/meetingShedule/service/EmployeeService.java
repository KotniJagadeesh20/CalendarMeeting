package com.example.meetingShedule.service;

import java.util.Optional;

import com.example.meetingShedule.Entity.Employee;

public interface EmployeeService {
	
	public Optional<Employee> getEmployee(String employeeId);

}
