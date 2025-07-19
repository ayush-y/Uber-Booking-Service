package org.example.uberbookingservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.Driver;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponseDto {

    private Long bookingId;

    private String bookingStatus;

    private Optional<Driver> driver;
}
