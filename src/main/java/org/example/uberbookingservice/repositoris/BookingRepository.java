package org.example.uberbookingservice.repositoris;

import org.example.uberprojectentityservice.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {


}
