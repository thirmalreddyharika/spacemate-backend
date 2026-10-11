package com.example.spacemate.controller;

import com.example.spacemate.dto.PaymentRequest;
import com.example.spacemate.entity.ParkingPass;
import com.example.spacemate.entity.Payment;
import com.example.spacemate.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> makePayment(
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.makePayment(request)
        );
    }

    @GetMapping(
            "/booking/{bookingId}/pass"
    )
    public ResponseEntity<ParkingPass>
    getParkingPass(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                paymentService.getParkingPass(
                        bookingId
                )
        );
    }
}