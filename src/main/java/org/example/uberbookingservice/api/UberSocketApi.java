package org.example.uberbookingservice.api;

import org.example.uberbookingservice.dto.DriverLocationDto;
import org.example.uberbookingservice.dto.NearbyDriversRequestDto;
import org.example.uberbookingservice.dto.RideRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UberSocketApi {


    @POST("/socket/api/newRide")
    Call<Boolean> raiseRideRequest(@Body RideRequestDto  rideRequestDto);

}