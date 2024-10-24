package com.example.meetingShedule.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.meetingShedule.Entity.Employee;
import com.example.meetingShedule.Entity.Meetings;
import com.example.meetingShedule.repository.EmployeeRepository;

public interface CalendarService {
	
	 public void bookMeeting(String employeeId, Meetings meeting);
	 
	 public List<LocalDateTime> findFreeSlots(String emp1, String emp2, int durationMinutes);
	 
	 public List<String> findMeetingConflicts(List<String> participants, Meetings newMeeting);

}
