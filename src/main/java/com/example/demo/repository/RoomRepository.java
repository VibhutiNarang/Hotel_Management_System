package com.example.demo.repository;

import com.example.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelAndIsAvailableTrue(String hotel);
    List<Room> findByHotel(String hotel);
}