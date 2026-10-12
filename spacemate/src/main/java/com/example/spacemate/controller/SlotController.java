package com.example.spacemate.controller;

import com.example.spacemate.dto.SlotRequest;
import com.example.spacemate.entity.Slot;
import com.example.spacemate.service.SlotService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping("/lot/{lotId}")
    public ResponseEntity<Slot> addSlot(
            @PathVariable Long lotId,
            @Valid @RequestBody SlotRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                slotService.addSlot(
                        lotId,
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/lot/{lotId}")
    public ResponseEntity<List<Slot>> getSlots(
            @PathVariable Long lotId) {

        return ResponseEntity.ok(
                slotService.getSlots(lotId)
        );
    }

    @PutMapping("/{slotId}/maintenance")
    public ResponseEntity<Slot> maintenance(
            @PathVariable Long slotId,
            Authentication authentication) {

        return ResponseEntity.ok(
                slotService.setMaintenance(
                        slotId,
                        authentication.getName()
                )
        );
    }

    @PutMapping("/{slotId}/available")
    public ResponseEntity<Slot> available(
            @PathVariable Long slotId,
            Authentication authentication) {

        return ResponseEntity.ok(
                slotService.makeAvailable(
                        slotId,
                        authentication.getName()
                )
        );
    }
}