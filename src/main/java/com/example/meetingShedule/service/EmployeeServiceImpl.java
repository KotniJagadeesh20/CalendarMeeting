package com.example.meetingShedule.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.meetingShedule.Entity.Employee;
import com.example.meetingShedule.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepository EmployeeRepo;
	
	 public Optional<Employee> getEmployee(String employeeId) {
	     return EmployeeRepo.findById(employeeId);
	 }
	 
	 

}
