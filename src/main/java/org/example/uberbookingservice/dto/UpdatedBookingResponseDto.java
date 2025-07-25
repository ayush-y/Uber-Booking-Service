package org.example.uberbookingservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectentityservice.models.Driver;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedBookingResponseDto {

    private Long  bookingId;

    private BookingStatus status;

    private Optional<Driver>  driver;
}
