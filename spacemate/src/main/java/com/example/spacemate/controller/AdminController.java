package com.example.spacemate.controller;

import com.example.spacemate.entity.ParkingLot;
import com.example.spacemate.entity.User;
import com.example.spacemate.enums.Role;
import com.example.spacemate.repository.BookingRepository;
import com.example.spacemate.repository.ParkingLotRepository;
import com.example.spacemate.repository.UserRepository;
import com.example.spacemate.service.ParkingLotService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final UserRepository userRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final BookingRepository bookingRepository;
    private final ParkingLotService parkingLotService;

    public AdminController(
            UserRepository userRepository,
            ParkingLotRepository parkingLotRepository,
            BookingRepository bookingRepository,
            ParkingLotService parkingLotService) {

        this.userRepository = userRepository;
        this.parkingLotRepository =
                parkingLotRepository;
        this.bookingRepository =
                bookingRepository;
        this.parkingLotService =
                parkingLotService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {

        return ResponseEntity.ok(
                userRepository.findByRole(
                        Role.USER
                )
        );
    }

    @GetMapping("/owners")
    public ResponseEntity<List<User>> getOwners() {

        return ResponseEntity.ok(
                userRepository.findByRole(
                        Role.OWNER
                )
        );
    }

    @GetMapping("/lots")
    public ResponseEntity<List<ParkingLot>>
    getLots() {

        return ResponseEntity.ok(
                parkingLotRepository.findAll()
        );
    }

    @PutMapping("/lots/{id}/approve")
    public ResponseEntity<ParkingLot>
    approveLot(@PathVariable Long id) {

        return ResponseEntity.ok(
                parkingLotService.approveLot(id)
        );
    }

    @PutMapping("/lots/{id}/reject")
    public ResponseEntity<ParkingLot>
    rejectLot(@PathVariable Long id) {

        return ResponseEntity.ok(
                parkingLotService.rejectLot(id)
        );
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookings() {

        return ResponseEntity.ok(
                bookingRepository.findAll()
        );
    }
}