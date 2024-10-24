package com.example.meetingShedule.request;

import java.time.LocalDateTime;
import java.util.List;

public class FreeSlotsResponse {
    private List<LocalDateTime> freeSlots;
    private String message;

    public FreeSlotsResponse(List<LocalDateTime> freeSlots, String message) {
        this.freeSlots = freeSlots;
        this.message = message;
    }

    public List<LocalDateTime> getFreeSlots() {
        return freeSlots;
    }

    public void setFreeSlots(List<LocalDateTime> freeSlots) {
        this.freeSlots = freeSlots;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
