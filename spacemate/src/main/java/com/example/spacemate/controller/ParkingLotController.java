package com.example.spacemate.controller;

import com.example.spacemate.dto.CreateParkingLotRequest;
import com.example.spacemate.entity.ParkingLot;
import com.example.spacemate.service.ParkingLotService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lots")
@CrossOrigin(origins = "http://localhost:5173")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(
            ParkingLotService parkingLotService) {

        this.parkingLotService =
                parkingLotService;
    }

    @PostMapping
    public ResponseEntity<ParkingLot> createLot(
            @Valid @RequestBody
            CreateParkingLotRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                parkingLotService.createLot(
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ParkingLot>>
    getApprovedLots() {

        return ResponseEntity.ok(
                parkingLotService.getApprovedLots()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParkingLot>>
    searchLots(
            @RequestParam String location) {

        return ResponseEntity.ok(
                parkingLotService.searchLots(location)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLot> getLot(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                parkingLotService.getLot(id)
        );
    }

    @GetMapping("/owner/my-lots")
    public ResponseEntity<List<ParkingLot>>
    getOwnerLots(Authentication authentication) {

        return ResponseEntity.ok(
                parkingLotService.getOwnerLots(
                        authentication.getName()
                )
        );
    }
}