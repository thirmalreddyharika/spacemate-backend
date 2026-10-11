package com.example.spacemate.repository;

import com.example.spacemate.entity.ParkingPass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingPassRepository
        extends JpaRepository<ParkingPass, Long> {

    Optional<ParkingPass> findByBookingId(Long bookingId);

    Optional<ParkingPass> findByQrCode(String qrCode);
}