package com.example.meetingShedule.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;

import com.example.meetingShedule.Entity.Employee;
import com.example.meetingShedule.Entity.Meetings;
import com.example.meetingShedule.request.ConflictRequest;
import com.example.meetingShedule.request.FreeSlotsRequest;
import com.example.meetingShedule.request.FreeSlotsResponse;
import com.example.meetingShedule.service.CalendarServiceImpl;
import com.example.meetingShedule.service.EmployeeService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

	private final CalendarServiceImpl calendarService;
	
	private final EmployeeService empService;
	
	public CalendarController(CalendarServiceImpl calendarService, EmployeeService service) {
		super();
		this.calendarService = calendarService;
		this.empService = service;
	}

	@PostMapping("/book/{employeeId}")
	public ResponseEntity<Map<String, String>> bookMeeting(@PathVariable String employeeId, @RequestBody Meetings meeting) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        calendarService.bookMeeting(employeeId, meeting);
	        response.put("message", "Meeting booked successfully.");
	        response.put("status", "success");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("message", "Failed to book the meeting: " + e.getMessage());
	        response.put("status", "error");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}



    @GetMapping("/free-slots")
    public ResponseEntity<FreeSlotsResponse> getFreeSlots(@RequestBody FreeSlotsRequest request) {
    	  if (request.getEmployees() == null || request.getEmployees().size() < 2) {
    	        return ResponseEntity.badRequest()
    	                .body(new FreeSlotsResponse(null, "Bad Request: At least 2 employee IDs must be provided.")); 
    	    }

    	   
    	    if (request.getDuration() <= 0) {
    	        return ResponseEntity.badRequest()
    	                .body(new FreeSlotsResponse(null, "Bad Request: Duration must be a positive value.")); 
    	    }

        Optional<Employee> employee1 = empService.getEmployee(request.getEmployees().get(0));
        Optional<Employee> employee2 = empService.getEmployee(request.getEmployees().get(1));

        if (!employee1.isPresent() || !employee2.isPresent()) {
            return ((BodyBuilder) ResponseEntity.notFound())
                    .body(new FreeSlotsResponse(null, "Not Found: One or both employee IDs are invalid."));
        }

        List<LocalDateTime> freeSlots = calendarService.findFreeSlots(employee1.get().getEmployeeId(), employee2.get().getEmployeeId(), request.getDuration());

        if (freeSlots.isEmpty()) {
            return ResponseEntity.ok(new FreeSlotsResponse(freeSlots, "No available free slots found.")); 
        }

        return ResponseEntity.ok(new FreeSlotsResponse(freeSlots, "The Exmployees are free in the given slots."));
    }

    @GetMapping("/conflicts")
    public ResponseEntity<Map<String, Object>> getMeetingConflicts(@RequestBody ConflictRequest request) {
        List<String> conflicts = calendarService.findMeetingConflicts(request.getParticipants(), request.getMeeting());
        Map<String, Object> response = new HashMap<>();

        if (conflicts.isEmpty()) {
            response.put("message", "No meeting conflicts found.");
            response.put("status", "success");
            response.put("conflicts", conflicts); 
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Conflicts found with the following meetings.");
            response.put("status", "conflict");
            response.put("conflicts", conflicts);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }


}
