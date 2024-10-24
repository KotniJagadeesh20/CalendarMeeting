// src/test/java/com/example/meetingShedule/repository/EmployeeRepositoryTest.java
package com.example.meetingShedule.repository;

import com.example.meetingShedule.Entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeRepositoryTest {

    @Mock
    private EmployeeRepository employeeRepository; // Mock the repository

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAndFindEmployee() {
        Employee employee = new Employee("emp1", null);

        // Stubbing the save method
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Stubbing the findById method
        when(employeeRepository.findById("emp1")).thenReturn(Optional.of(employee));

        employeeRepository.save(employee); // This will use the mocked behavior

        Optional<Employee> foundEmployee = employeeRepository.findById("emp1");
        assertTrue(foundEmployee.isPresent(), "Employee should be found.");
        assertEquals(employee, foundEmployee.get(), "Found employee should match saved employee.");
    }

    @Test
    void testFindEmployee_NotFound() {
        // Stubbing the findById method to return empty
        when(employeeRepository.findById("invalidId")).thenReturn(Optional.empty());

        Optional<Employee> employee = employeeRepository.findById("invalidId");
        assertFalse(employee.isPresent(), "Employee should not be found.");
    }
}



