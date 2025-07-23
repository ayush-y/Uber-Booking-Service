package org.example.uberbookingservice.services;

import org.example.uberbookingservice.api.LocationServiceApi;
import org.example.uberbookingservice.api.UberSocketApi;
import org.example.uberbookingservice.dto.*;
import org.example.uberbookingservice.repositoris.BookingRepository;
import org.example.uberbookingservice.repositoris.DriverRepository;
import org.example.uberbookingservice.repositoris.PassengerRepository;
import org.example.uberprojectentityservice.models.Booking;
import org.example.uberprojectentityservice.models.BookingStatus;
import org.example.uberprojectentityservice.models.Driver;
import org.example.uberprojectentityservice.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.awt.print.Book;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class BookingServiceImpl implements BookingService{

    private final PassengerRepository passengerRepository;

    private final BookingRepository bookingRepository;

    private final RestTemplate restTemplate;

    private final LocationServiceApi  locationServiceApi;
    private final DriverRepository driverRepository;

    private final UberSocketApi uberSocketApi;


    //private static final String LOCATION_SERVICE = "http://localhost:7777";

    public BookingServiceImpl(PassengerRepository passengerRepository,UberSocketApi uberSocketApi,
                              BookingRepository bookingRepository, LocationServiceApi locationServiceApi, DriverRepository driverRepository) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = new RestTemplate();
        this.locationServiceApi = locationServiceApi;
        this.driverRepository = driverRepository;
        this.uberSocketApi = uberSocketApi;
    }



    @Override
    public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails) {
        Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(bookingDetails.getStartLocation())
//                .endLocation(bookingDetails.getEndLocation())
                .passenger(passenger.get())
                .build();
        Booking newBooking = bookingRepository.save(booking);

        // make an api call to location service to fetch nearby drivers

        NearbyDriversRequestDto request = NearbyDriversRequestDto.builder()
                .latitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();

        processNearByDriverAsync(request, bookingDetails.getPassengerId(), newBooking.getId());

//
//        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(
//                LOCATION_SERVICE + "/api/location/nearby/drivers", request, DriverLocationDto[].class);
//
//        if(result.getStatusCode().is2xxSuccessful() && result.getBody() != null) {
//            List<DriverLocationDto> driverLocations = Arrays.asList(result.getBody());
//            driverLocations.forEach(driverLocationDto -> {
//                System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
//            });
//        }

        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }
    @Override
    public UpdatedBookingResponseDto updateBooking(UpdatedBookingRequestDto bookingRequestDto, Long BookingId) {

        Optional<Driver> driver = driverRepository.findById(bookingRequestDto.getDriverId().get());
        //Todo : if(driver.isPresent() && driver.get.isAvailable())
        bookingRepository.updateBookingStatusAndDriverById(BookingId, BookingStatus.SCHEDULED, driver.get());
        //Todo : driverRepository.update -> make this unavailable
        Optional<Booking> booking = bookingRepository.findById(BookingId);
        return UpdatedBookingResponseDto.builder()
                .bookingId(BookingId)
                .status(booking.get().getBookingStatus())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();
    }


    private void processNearByDriverAsync(NearbyDriversRequestDto requestDto, Long passengerId, Long bookingId) {
        Call<DriverLocationDto[]> call = locationServiceApi.getNearByDrivers(requestDto);
//        try{
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }


            call.enqueue(new Callback<DriverLocationDto[]>() {
                @Override
                public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        List<DriverLocationDto> driverLocations = Arrays.asList(response.body());
                        driverLocations.forEach(driverLocationDto -> {
                            System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
                        });

                        raiseRideRequestAsync(RideRequestDto.builder().passengerId(passengerId).bookingId(bookingId).build());

                    }else{
                        System.out.println("Request failed " + response.message());
                    }
                }


                @Override
                public void onFailure(Call<DriverLocationDto[]> call, Throwable t) {
                    t.printStackTrace();
                }
            });


    }
    private void raiseRideRequestAsync(RideRequestDto requestDto) {
        Call<Boolean> call = uberSocketApi.raiseRideRequest(requestDto);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Boolean result = response.body();
                    System.out.println("Driver response is "+ result.toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("Request failed " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
