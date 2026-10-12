package com.example.spacemate.service;

import com.example.spacemate.dto.CreateParkingLotRequest;
import com.example.spacemate.entity.ParkingLot;
import com.example.spacemate.entity.User;
import com.example.spacemate.enums.LotStatus;
import com.example.spacemate.enums.Role;
import com.example.spacemate.exception.BadRequestException;
import com.example.spacemate.exception.ResourceNotFoundException;
import com.example.spacemate.repository.ParkingLotRepository;
import com.example.spacemate.repository.UserRepository;

import org.springframework.stereotype.Service;

import com.example.spacemate.entity.Slot;
import com.example.spacemate.enums.SlotStatus;
import com.example.spacemate.repository.SlotRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;

    public ParkingLotService(
            ParkingLotRepository parkingLotRepository,
            UserRepository userRepository,
            SlotRepository slotRepository) {

        this.parkingLotRepository = parkingLotRepository;
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
    }

    public ParkingLot createLot(
            CreateParkingLotRequest request,
            String ownerEmail) {

        User owner =
                userRepository.findByEmail(ownerEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner not found"
                                )
                        );

        if (owner.getRole() != Role.OWNER) {
            throw new BadRequestException(
                    "Only parking lot owners can create lots"
            );
        }

        ParkingLot lot = new ParkingLot();

        lot.setName(request.getName());
        lot.setLocation(request.getLocation());
        lot.setTotalSlots(request.getTotalSlots());
        lot.setPricePerHour(
                request.getPricePerHour()
        );

        // Admin approval required
        lot.setStatus(LotStatus.PENDING);

        lot.setOwner(owner);

        ParkingLot savedLot = parkingLotRepository.save(lot);

        int count = request.getTotalSlots() != null ? request.getTotalSlots() : 0;
        if (count > 0 && count <= 50) {
            List<Slot> slots = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                Slot slot = new Slot();
                slot.setSlotNumber(String.format("S-%02d", i));
                slot.setAvailable(true);
                slot.setStatus(SlotStatus.AVAILABLE);
                slot.setParkingLot(savedLot);
                slots.add(slot);
            }
            slotRepository.saveAll(slots);
        }

        return savedLot;
    }

    public List<ParkingLot> getApprovedLots() {

        return parkingLotRepository
                .findByStatus(LotStatus.APPROVED);
    }

    public List<ParkingLot> searchLots(
            String location) {

        return parkingLotRepository
                .findByLocationContainingIgnoreCase(
                        location
                );
    }

    public List<ParkingLot> getOwnerLots(
            String email) {

        User owner =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner not found"
                                )
                        );

        return parkingLotRepository
                .findByOwnerId(owner.getId());
    }

    public ParkingLot getLot(Long id) {

        return parkingLotRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parking lot not found"
                        )
                );
    }

    public ParkingLot approveLot(Long id) {

        ParkingLot lot = getLot(id);

        lot.setStatus(LotStatus.APPROVED);

        return parkingLotRepository.save(lot);
    }

    public ParkingLot rejectLot(Long id) {

        ParkingLot lot = getLot(id);

        lot.setStatus(LotStatus.REJECTED);

        return parkingLotRepository.save(lot);
    }
}