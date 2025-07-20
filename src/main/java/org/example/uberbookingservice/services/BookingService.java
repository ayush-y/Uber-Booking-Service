package org.example.uberbookingservice.services;

import org.example.uberbookingservice.dto.CreateBookingDto;
import org.example.uberbookingservice.dto.CreateBookingResponseDto;
import org.example.uberbookingservice.dto.UpdatedBookingRequestDto;
import org.example.uberbookingservice.dto.UpdatedBookingResponseDto;
import org.example.uberprojectentityservice.models.Booking;

public interface BookingService {

    CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);

    UpdatedBookingResponseDto updateBooking(UpdatedBookingRequestDto bookingRequestDto, Long BookingId);
}
