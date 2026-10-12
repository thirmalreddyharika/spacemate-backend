package com.example.spacemate.config;

import com.example.spacemate.entity.Booking;
import com.example.spacemate.entity.ParkingLot;
import com.example.spacemate.entity.Slot;
import com.example.spacemate.entity.User;
import com.example.spacemate.enums.BookingStatus;
import com.example.spacemate.enums.LotStatus;
import com.example.spacemate.enums.Role;
import com.example.spacemate.enums.SlotStatus;
import com.example.spacemate.repository.BookingRepository;
import com.example.spacemate.repository.ParkingLotRepository;
import com.example.spacemate.repository.SlotRepository;
import com.example.spacemate.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            ParkingLotRepository parkingLotRepository,
            SlotRepository slotRepository,
            BookingRepository bookingRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // 1. Seed Admin
            if (!userRepository.existsByEmail("admin@spacemate.com")) {
                User admin = new User();
                admin.setName("SpaceMate Admin");
                admin.setEmail("admin@spacemate.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setPhone("9999999999");
                userRepository.save(admin);
            }

            // 2. Seed Demo Owner
            User owner = userRepository.findByEmail("owner@spacemate.com").orElse(null);
            if (owner == null) {
                owner = new User();
                owner.setName("Rajesh Sharma (Owner)");
                owner.setEmail("owner@spacemate.com");
                owner.setPassword(passwordEncoder.encode("owner123"));
                owner.setRole(Role.OWNER);
                owner.setPhone("9876543210");
                owner = userRepository.save(owner);
            }

            // 3. Seed Demo User
            User demoUser = userRepository.findByEmail("user@spacemate.com").orElse(null);
            if (demoUser == null) {
                demoUser = new User();
                demoUser.setName("Amit Kumar");
                demoUser.setEmail("user@spacemate.com");
                demoUser.setPassword(passwordEncoder.encode("user123"));
                demoUser.setRole(Role.USER);
                demoUser.setPhone("9123456780");
                demoUser = userRepository.save(demoUser);
            }

            // 4. Seed Parking Lots & Slots if none exist
            if (parkingLotRepository.count() == 0) {
                // Lot 1: City Center Grand Parking
                ParkingLot lot1 = new ParkingLot();
                lot1.setName("City Center Grand Parking");
                lot1.setLocation("City Center, Commercial Road");
                lot1.setTotalSlots(10);
                lot1.setPricePerHour(BigDecimal.valueOf(40.00));
                lot1.setStatus(LotStatus.APPROVED);
                lot1.setOwner(owner);
                lot1 = parkingLotRepository.save(lot1);

                List<Slot> lot1Slots = new ArrayList<>();
                for (int i = 1; i <= 8; i++) {
                    Slot slot = new Slot();
                    slot.setSlotNumber("A-0" + i);
                    slot.setAvailable(true);
                    slot.setStatus(SlotStatus.AVAILABLE);
                    slot.setParkingLot(lot1);
                    lot1Slots.add(slot);
                }
                slotRepository.saveAll(lot1Slots);

                // Lot 2: Metro Station Plaza Parking
                ParkingLot lot2 = new ParkingLot();
                lot2.setName("Metro Station Plaza Parking");
                lot2.setLocation("Metro Blue Line, Station Gate 2");
                lot2.setTotalSlots(8);
                lot2.setPricePerHour(BigDecimal.valueOf(30.00));
                lot2.setStatus(LotStatus.APPROVED);
                lot2.setOwner(owner);
                lot2 = parkingLotRepository.save(lot2);

                List<Slot> lot2Slots = new ArrayList<>();
                for (int i = 1; i <= 6; i++) {
                    Slot slot = new Slot();
                    slot.setSlotNumber("M-" + (100 + i));
                    slot.setAvailable(true);
                    slot.setStatus(SlotStatus.AVAILABLE);
                    slot.setParkingLot(lot2);
                    lot2Slots.add(slot);
                }
                slotRepository.saveAll(lot2Slots);

                // Lot 3: Tech Park Multi-Level Parking
                ParkingLot lot3 = new ParkingLot();
                lot3.setName("Tech Hub Multi-Level Parking");
                lot3.setLocation("IT Expressway, Silicon Zone");
                lot3.setTotalSlots(12);
                lot3.setPricePerHour(BigDecimal.valueOf(50.00));
                lot3.setStatus(LotStatus.APPROVED);
                lot3.setOwner(owner);
                lot3 = parkingLotRepository.save(lot3);

                List<Slot> lot3Slots = new ArrayList<>();
                for (int i = 1; i <= 6; i++) {
                    Slot slot = new Slot();
                    slot.setSlotNumber("T-" + (200 + i));
                    slot.setAvailable(true);
                    slot.setStatus(SlotStatus.AVAILABLE);
                    slot.setParkingLot(lot3);
                    lot3Slots.add(slot);
                }
                slotRepository.saveAll(lot3Slots);

                // 5. Seed a sample Confirmed Booking for demoUser
                if (!lot1Slots.isEmpty()) {
                    Slot bookedSlot = lot1Slots.get(0);
                    Booking sampleBooking = new Booking();
                    sampleBooking.setUser(demoUser);
                    sampleBooking.setSlot(bookedSlot);
                    sampleBooking.setBookingDate(LocalDate.now());
                    sampleBooking.setStartTime(LocalTime.of(10, 0));
                    sampleBooking.setEndTime(LocalTime.of(12, 0));
                    sampleBooking.setAmount(BigDecimal.valueOf(80.00));
                    sampleBooking.setStatus(BookingStatus.CONFIRMED);
                    bookingRepository.save(sampleBooking);
                }
            }
        };
    }
}