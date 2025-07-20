package org.example.uberbookingservice.controller;


import org.example.uberbookingservice.dto.CreateBookingDto;
import org.example.uberbookingservice.dto.CreateBookingResponseDto;
import org.example.uberbookingservice.dto.UpdatedBookingRequestDto;
import org.example.uberbookingservice.dto.UpdatedBookingResponseDto;
import org.example.uberbookingservice.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<CreateBookingResponseDto> createBooking(@RequestBody CreateBookingDto createBookingDto) {

        return new ResponseEntity<>(bookingService.createBooking(createBookingDto), HttpStatus.CREATED);
    }
    @PatchMapping("/{bookingId}")
    public ResponseEntity<UpdatedBookingResponseDto> updateBooking(@RequestBody UpdatedBookingRequestDto requestDto, @PathVariable Long bookingId) {
        return new ResponseEntity<>(bookingService.updateBooking(requestDto, bookingId), HttpStatus.OK);
    }

}

