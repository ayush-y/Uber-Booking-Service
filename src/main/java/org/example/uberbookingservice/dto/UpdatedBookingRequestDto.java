package org.example.uberbookingservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.BookingStatus;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedBookingRequestDto {

    private String status;

    private Optional<Long> driverId;
}
