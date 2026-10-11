package com.example.spacemate.controller;

import com.example.spacemate.dto.BookingRequest;
import com.example.spacemate.entity.Booking;
import com.example.spacemate.service.BookingService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                bookingService.createBooking(
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>>
    getMyBookings(
            Authentication authentication) {

        return ResponseEntity.ok(
                bookingService.getMyBookings(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBooking(id)
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(
                        id,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/owner/my-bookings")
    public ResponseEntity<List<Booking>>
    getOwnerBookings(
            Authentication authentication) {

        return ResponseEntity.ok(
                bookingService.getOwnerBookings(
                        authentication.getName()
                )
        );
    }
}