package com.example.spacemate.dto;

import jakarta.validation.constraints.NotBlank;

public class SlotRequest {

    @NotBlank
    private String slotNumber;

    public SlotRequest() {
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }
}