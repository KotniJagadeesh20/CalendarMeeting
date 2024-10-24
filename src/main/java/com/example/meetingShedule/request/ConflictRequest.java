package com.example.meetingShedule.request;

import java.util.List;

import com.example.meetingShedule.Entity.Meetings;

public class ConflictRequest {
    private List<String> participants;
    private Meetings meeting;

    // Getters and setters
    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Meetings getMeeting() {
        return meeting;
    }

    public void setMeeting(Meetings meeting) {
        this.meeting = meeting;
    }
}

