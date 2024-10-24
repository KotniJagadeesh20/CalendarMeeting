package com.example.meetingShedule.Entity;



import jakarta.persistence.*;
import java.util.List;

@Entity
public class Employee {

    @Id
    private String employeeId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private List<Meetings> meetings;

    // Constructors
    public Employee() {}

    public Employee(String employeeId, List<Meetings> meetings) {
        this.employeeId = employeeId;
        this.meetings = meetings;
    }

    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<Meetings> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meetings> meetings) {
        this.meetings = meetings;
    }
}

