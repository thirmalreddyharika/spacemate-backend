package com.example.spacemate.repository;

import com.example.spacemate.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository
        extends JpaRepository<Slot, Long> {

    List<Slot> findByParkingLotId(Long parkingLotId);

    boolean existsByParkingLotIdAndSlotNumber(
            Long parkingLotId,
            String slotNumber
    );
}