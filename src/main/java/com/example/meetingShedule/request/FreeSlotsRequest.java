package com.example.meetingShedule.request;

import java.util.List;


public class FreeSlotsRequest {
    
    private List<String> employees;  // Keep the field name consistent with JSON

    private int duration;

    // Change the getter method to match the field name
    public List<String> getEmployees() {  // Change here
        return employees;
    }

    // Update the setter to match the field name
    public void setEmployees(List<String> employees) {  // Change here
        this.employees = employees;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

