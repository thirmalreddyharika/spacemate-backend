package com.example.spacemate.repository;

import com.example.spacemate.entity.Booking;
import com.example.spacemate.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByBookingDateDesc(
            Long userId
    );

    List<Booking> findBySlotIdAndBookingDate(
            Long slotId,
            LocalDate bookingDate
    );

    List<Booking> findBySlotParkingLotOwnerId(
            Long ownerId
    );

    boolean existsBySlotIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
            Long slotId,
            LocalDate bookingDate,
            LocalTime endTime,
            LocalTime startTime,
            Collection<BookingStatus> statuses
    );
}