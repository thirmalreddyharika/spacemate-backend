package com.example.spacemate.service;

import com.example.spacemate.dto.PaymentRequest;
import com.example.spacemate.entity.Booking;
import com.example.spacemate.entity.Payment;
import com.example.spacemate.entity.ParkingPass;
import com.example.spacemate.enums.BookingStatus;
import com.example.spacemate.enums.PassStatus;
import com.example.spacemate.enums.PaymentStatus;
import com.example.spacemate.exception.BadRequestException;
import com.example.spacemate.exception.ResourceNotFoundException;
import com.example.spacemate.repository.BookingRepository;
import com.example.spacemate.repository.ParkingPassRepository;
import com.example.spacemate.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ParkingPassRepository parkingPassRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            ParkingPassRepository parkingPassRepository) {

        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.parkingPassRepository =
                parkingPassRepository;
    }

    @Transactional
    public Payment makePayment(
            PaymentRequest request) {

        Booking booking =
                bookingRepository.findById(
                        request.getBookingId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found"
                        )
                );

        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            throw new BadRequestException(
                    "Cannot pay for cancelled booking"
            );
        }

        if (paymentRepository
                .findByBookingId(
                        booking.getId()
                ).isPresent()) {

            throw new BadRequestException(
                    "Payment already exists"
            );
        }

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(
                booking.getAmount()
        );

        // Simulated successful payment
        payment.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        payment.setTransactionId(
                "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        Payment savedPayment =
                paymentRepository.save(payment);

        booking.setStatus(
                BookingStatus.CONFIRMED
        );

        bookingRepository.save(booking);

        generateParkingPass(booking);

        return savedPayment;
    }

    private void generateParkingPass(
            Booking booking) {

        ParkingPass pass =
                parkingPassRepository
                        .findByBookingId(
                                booking.getId()
                        )
                        .orElse(null);

        if (pass != null) {
            return;
        }

        pass = new ParkingPass();

        pass.setBooking(booking);

        pass.setQrCode(
                "SPACEMATE-" +
                UUID.randomUUID()
                        .toString()
                        .toUpperCase()
        );

        pass.setStatus(
                PassStatus.ACTIVE
        );

        parkingPassRepository.save(pass);
    }

    public ParkingPass getParkingPass(
            Long bookingId) {

        return parkingPassRepository
                .findByBookingId(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parking pass not found"
                        )
                );
    }
}