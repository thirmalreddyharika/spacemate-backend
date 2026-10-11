package com.example.spacemate.repository;

import com.example.spacemate.entity.ParkingLot;
import com.example.spacemate.enums.LotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingLotRepository
        extends JpaRepository<ParkingLot, Long> {

    List<ParkingLot> findByStatus(LotStatus status);

    List<ParkingLot> findByOwnerId(Long ownerId);

    List<ParkingLot> findByLocationContainingIgnoreCase(
            String location
    );
}