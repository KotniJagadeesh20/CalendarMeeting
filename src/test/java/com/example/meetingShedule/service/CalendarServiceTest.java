// src/test/java/com/example/meetingShedule/service/CalendarServiceTest.java
package com.example.meetingShedule.service;

import com.example.meetingShedule.Entity.Employee;
import com.example.meetingShedule.Entity.Meetings;
import com.example.meetingShedule.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeService employeeService; // Mocking EmployeeService

    @InjectMocks
    private CalendarServiceImpl calendarService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testBookMeeting_Success() {
        Employee employee = new Employee("emp1", new ArrayList<>());
        Meetings meeting = new Meetings(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Team Meeting");

        // Mocking the behavior of EmployeeService
        when(employeeService.getEmployee("emp1")).thenReturn(Optional.of(employee));

        calendarService.bookMeeting(employee.getEmployeeId(), meeting);

        assertEquals(1, employee.getMeetings().size(), "Meeting should be added successfully.");
        assertEquals(meeting, employee.getMeetings().get(0), "Meeting details should match.");
        verify(employeeRepository, times(1)).save(employee); // Verify save was called
    }

    @Test
    void testBookMeeting_Failure_EmployeeNotFound() {
        Meetings meeting = new Meetings(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Team Meeting");

        // Mocking the behavior of EmployeeService to return empty
        when(employeeService.getEmployee("invalidEmployee")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                calendarService.bookMeeting("invalidEmployee", meeting));

        assertEquals("Employee not found with ID: invalidEmployee", exception.getMessage());
    }

    @Test
    void testFindFreeSlots() {
        Employee emp1 = new Employee("emp1", List.of(
                new Meetings(LocalDateTime.of(2024, 10, 23, 10, 0),
                        LocalDateTime.of(2024, 10, 23, 11, 0), "Morning Meeting")
        ));
        Employee emp2 = new Employee("emp2", List.of(
                new Meetings(LocalDateTime.of(2024, 10, 23, 9, 0),
                        LocalDateTime.of(2024, 10, 23, 9, 30), "Early Meeting")
        ));

        // Mocking repository behavior
        when(employeeRepository.findById("emp1")).thenReturn(Optional.of(emp1));
        when(employeeRepository.findById("emp2")).thenReturn(Optional.of(emp2));

        List<LocalDateTime> freeSlots = calendarService.findFreeSlots("emp1", "emp2", 30);

        assertFalse(freeSlots.isEmpty(), "There should be available free slots.");
        assertEquals(LocalDateTime.of(2024, 10, 23, 9, 30), freeSlots.get(0),
                "First available slot should be at 9:30 AM.");
    }

    @Test
    void testFindMeetingConflicts() {
        // Arrange: Setup existing meeting and employee
        Meetings existingMeeting = new Meetings(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Team Meeting");
        Employee employee = new Employee("emp1", new ArrayList<>(List.of(existingMeeting)));

        // Mock the repository to return the employee with the existing meeting
        when(employeeRepository.findAllById(List.of("emp1"))).thenReturn(List.of(employee));

        // Create a new meeting that conflicts with the existing one
        Meetings newMeeting = new Meetings(LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusHours(2), "New Meeting");

        // Act: Call the method under test
        List<String> conflicts = calendarService.findMeetingConflicts(List.of("emp1"), newMeeting);

        // Assert: Verify the results
        assertEquals(1, conflicts.size(), "There should be 1 conflict.");
        assertEquals("emp1", conflicts.get(0), "Conflict should be with emp1.");
    }

    @Test
    void testNoMeetingConflicts() {
        // Arrange: Setup an employee with no existing meetings
        Employee employee = new Employee("emp1", new ArrayList<>());
        when(employeeRepository.findAllById(List.of("emp1"))).thenReturn(List.of(employee));

        // Create a new meeting that does not conflict
        Meetings newMeeting = new Meetings(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), "New Meeting");

        // Act: Call the method under test
        List<String> conflicts = calendarService.findMeetingConflicts(List.of("emp1"), newMeeting);

        // Assert: Verify that there are no conflicts
        assertEquals(0, conflicts.size(), "There should be no conflicts.");
    }
}


