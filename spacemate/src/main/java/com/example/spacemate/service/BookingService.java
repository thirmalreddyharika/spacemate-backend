package com.example.spacemate.service;

import com.example.spacemate.dto.BookingRequest;
import com.example.spacemate.entity.Booking;
import com.example.spacemate.entity.Slot;
import com.example.spacemate.entity.User;
import com.example.spacemate.enums.BookingStatus;
import com.example.spacemate.enums.SlotStatus;
import com.example.spacemate.exception.BadRequestException;
import com.example.spacemate.exception.ResourceNotFoundException;
import com.example.spacemate.repository.BookingRepository;
import com.example.spacemate.repository.SlotRepository;
import com.example.spacemate.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            SlotRepository slotRepository,
            UserRepository userRepository) {

        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking createBooking(
            BookingRequest request,
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Slot slot =
                slotRepository.findById(
                        request.getSlotId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Slot not found"
                        )
                );

        if (slot.getStatus()
                == SlotStatus.MAINTENANCE) {

            throw new BadRequestException(
                    "Slot is under maintenance"
            );
        }

        if (request.getStartTime()
                .isAfter(request.getEndTime())
                ||
                request.getStartTime()
                        .equals(request.getEndTime())) {

            throw new BadRequestException(
                    "Invalid start and end time"
            );
        }

        List<BookingStatus> activeStatuses =
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.CONFIRMED
                );

        boolean alreadyBooked =
                bookingRepository
                        .existsBySlotIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
                                slot.getId(),
                                request.getBookingDate(),
                                request.getEndTime(),
                                request.getStartTime(),
                                activeStatuses
                        );

        if (alreadyBooked) {

            throw new BadRequestException(
                    "Slot already booked for this time"
            );
        }

        long minutes =
                Duration.between(
                        request.getStartTime(),
                        request.getEndTime()
                ).toMinutes();

        double hours =
                Math.ceil(minutes / 60.0);

        BigDecimal amount =
                slot.getParkingLot()
                        .getPricePerHour()
                        .multiply(
                                BigDecimal.valueOf(hours)
                        );

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setSlot(slot);
        booking.setBookingDate(
                request.getBookingDate()
        );
        booking.setStartTime(
                request.getStartTime()
        );
        booking.setEndTime(
                request.getEndTime()
        );
        booking.setAmount(amount);

        booking.setStatus(
                BookingStatus.PENDING
        );

        return bookingRepository.save(booking);
    }

    public List<Booking> getMyBookings(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return bookingRepository
                .findByUserIdOrderByBookingDateDesc(
                        user.getId()
                );
    }

    public Booking getBooking(Long id) {

        return bookingRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found"
                        )
                );
    }

    public Booking confirmBooking(Long id) {

        Booking booking = getBooking(id);

        booking.setStatus(
                BookingStatus.CONFIRMED
        );

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(
            Long id,
            String email) {

        Booking booking = getBooking(id);

        if (!booking.getUser()
                .getEmail()
                .equals(email)) {

            throw new BadRequestException(
                    "You cannot cancel this booking"
            );
        }

        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            throw new BadRequestException(
                    "Booking already cancelled"
            );
        }

        booking.setStatus(
                BookingStatus.CANCELLED
        );

        return bookingRepository.save(booking);
    }

    public List<Booking> getOwnerBookings(
            String email) {

        User owner =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner not found"
                                )
                        );

        return bookingRepository
                .findBySlotParkingLotOwnerId(
                        owner.getId()
                );
    }
}