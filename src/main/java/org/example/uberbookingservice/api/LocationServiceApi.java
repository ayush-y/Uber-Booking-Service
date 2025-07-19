package org.example.uberbookingservice.api;

import org.example.uberbookingservice.dto.DriverLocationDto;
import org.example.uberbookingservice.dto.NearbyDriversRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {


    @POST("/api/location/nearby/drivers")
    Call<DriverLocationDto[]> getDriverLocations(@Body NearbyDriversRequestDto nearbyDriversRequestDto);

}
