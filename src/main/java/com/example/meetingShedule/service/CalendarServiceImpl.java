package com.example.meetingShedule.service;

import org.springframework.stereotype.Service;

import com.example.meetingShedule.Entity.Employee;
import com.example.meetingShedule.Entity.Meetings;
import com.example.meetingShedule.repository.EmployeeRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarServiceImpl implements CalendarService {

	private final EmployeeRepository repository;

	private EmployeeService employeeService;

	public CalendarServiceImpl(EmployeeRepository repository, EmployeeService employeeService) {
		super();
		this.repository = repository;
		this.employeeService = employeeService;
	}

	
	@Override
	public void bookMeeting(String employeeId, Meetings meeting) {

		Employee employee = employeeService.getEmployee(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
		employee.getMeetings().add(meeting);
		repository.save(employee);
	}

	@Override
	public List<LocalDateTime> findFreeSlots(String emp1, String emp2, int durationMinutes) {
	    List<LocalDateTime> freeSlots = new ArrayList<>();

	    Optional<Employee> employee1 = repository.findById(emp1);
	    Optional<Employee> employee2 = repository.findById(emp2);

	    if (!employee1.isPresent() || !employee2.isPresent()) {
	        return freeSlots; 
	    }

	    List<Meetings> emp1Meetings = employee1.get().getMeetings();
	    List<Meetings> emp2Meetings = employee2.get().getMeetings();

	    // Combine and sort meetings
	    List<Meetings> allMeetings = new ArrayList<>();
	    allMeetings.addAll(emp1Meetings);
	    allMeetings.addAll(emp2Meetings);
	    Collections.sort(allMeetings, Comparator.comparing(Meetings::getStartTime));

	    LocalDateTime lastEndTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0)); 
	    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0)); 

	    for (Meetings meeting : allMeetings) {
	        LocalDateTime startTime = meeting.getStartTime();
	        LocalDateTime endTime = meeting.getEndTime();

	        // Check for free slots before the current meeting
	        while (lastEndTime.plusMinutes(durationMinutes).isBefore(startTime)) {
	            freeSlots.add(lastEndTime);
	            lastEndTime = lastEndTime.plusMinutes(30); // Add next slot
	        }

	        // Move lastEndTime to the end of the current meeting
	        lastEndTime = endTime.isAfter(lastEndTime) ? endTime : lastEndTime;
	    }

	    // Check for remaining free slots until the end of the day
	    while (lastEndTime.plusMinutes(durationMinutes).isBefore(endOfDay)) {
	        freeSlots.add(lastEndTime);
	        lastEndTime = lastEndTime.plusMinutes(30); // Add next slot
	    }

	    return freeSlots;
	}




	
	@Override
	public List<String> findMeetingConflicts(List<String> participants, Meetings newMeeting) {
		List<String> conflictingEmployees = new ArrayList<>();

		List<Employee> Employees = repository.findAllById(participants);
		for (Employee employee : Employees) {
			for (Meetings meeting : employee.getMeetings()) {
				if (newMeeting.getStartTime().isBefore(meeting.getEndTime())
						&& newMeeting.getEndTime().isAfter(meeting.getStartTime())) {
					conflictingEmployees.add(employee.getEmployeeId());
				}
			}
		}
		return conflictingEmployees;
	}

	
}
