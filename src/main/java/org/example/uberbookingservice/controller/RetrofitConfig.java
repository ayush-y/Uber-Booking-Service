package org.example.uberbookingservice.controller;


import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.example.uberbookingservice.api.LocationServiceApi;
import org.example.uberbookingservice.api.UberSocketApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Autowired
    private EurekaClient eurekaClient;

    private String getServiceUrl(String serviceName) {
        return eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi locationServiceApi() {

        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("UBERPROJECT-LOCATIONSERVICE")) //Used eureka service to async communication b/w two microservices
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);
    }
    @Bean
    public UberSocketApi uberSocketApi() {

        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("UBERSOCKETSERVICE")) //Used eureka service to async communication b/w two microservices
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(UberSocketApi.class);
    }
}
