package com.example.spacemate.service;

import com.example.spacemate.dto.SlotRequest;
import com.example.spacemate.entity.ParkingLot;
import com.example.spacemate.entity.Slot;
import com.example.spacemate.entity.User;
import com.example.spacemate.enums.SlotStatus;
import com.example.spacemate.exception.BadRequestException;
import com.example.spacemate.exception.ResourceNotFoundException;
import com.example.spacemate.repository.ParkingLotRepository;
import com.example.spacemate.repository.SlotRepository;
import com.example.spacemate.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final UserRepository userRepository;

    public SlotService(
            SlotRepository slotRepository,
            ParkingLotRepository parkingLotRepository,
            UserRepository userRepository) {

        this.slotRepository = slotRepository;
        this.parkingLotRepository =
                parkingLotRepository;
        this.userRepository = userRepository;
    }

    public Slot addSlot(
            Long lotId,
            SlotRequest request,
            String ownerEmail) {

        ParkingLot lot =
                parkingLotRepository.findById(lotId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Parking lot not found"
                                )
                        );

        User owner =
                userRepository.findByEmail(ownerEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner not found"
                                )
                        );

        if (!lot.getOwner().getId()
                .equals(owner.getId())) {

            throw new BadRequestException(
                    "You do not own this parking lot"
            );
        }

        if (slotRepository
                .existsByParkingLotIdAndSlotNumber(
                        lotId,
                        request.getSlotNumber()
                )) {

            throw new BadRequestException(
                    "Slot already exists"
            );
        }

        List<Slot> existingSlots =
                slotRepository.findByParkingLotId(lotId);

        if (existingSlots.size()
                >= lot.getTotalSlots()) {

            throw new BadRequestException(
                    "Maximum number of slots reached"
            );
        }

        Slot slot = new Slot();

        slot.setSlotNumber(
                request.getSlotNumber()
        );

        slot.setAvailable(true);
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setParkingLot(lot);

        return slotRepository.save(slot);
    }

    public List<Slot> getSlots(Long lotId) {

        return slotRepository
                .findByParkingLotId(lotId);
    }

    public Slot getSlot(Long id) {

        return slotRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Slot not found"
                        )
                );
    }

    public Slot setMaintenance(
            Long slotId,
            String ownerEmail) {

        Slot slot = getSlot(slotId);

        if (!slot.getParkingLot()
                .getOwner()
                .getEmail()
                .equals(ownerEmail)) {

            throw new BadRequestException(
                    "You do not own this slot"
            );
        }

        slot.setStatus(
                SlotStatus.MAINTENANCE
        );

        slot.setAvailable(false);

        return slotRepository.save(slot);
    }

    public Slot makeAvailable(
            Long slotId,
            String ownerEmail) {

        Slot slot = getSlot(slotId);

        if (!slot.getParkingLot()
                .getOwner()
                .getEmail()
                .equals(ownerEmail)) {

            throw new BadRequestException(
                    "You do not own this slot"
            );
        }

        slot.setStatus(
                SlotStatus.AVAILABLE
        );

        slot.setAvailable(true);

        return slotRepository.save(slot);
    }
}