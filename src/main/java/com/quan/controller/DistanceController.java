package com.quan.controller;

import com.quan.dto.RestaurantDTO;
import com.quan.model.Address;
import com.quan.model.Food;
import com.quan.model.Restaurant;
import com.quan.repository.RestaurantRepository;
import com.quan.request.AddressCustomerRequest;
import com.quan.service.OSMGeocodingService;
import com.quan.service.OSRMDistanceService;
import com.quan.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/distance")
@Slf4j
public class DistanceController {

    private final OSMGeocodingService geocodingService;
    private final OSRMDistanceService distanceService;

    public DistanceController(OSMGeocodingService geocodingService, OSRMDistanceService distanceService) {
        this.geocodingService = geocodingService;
        this.distanceService = distanceService;
    }

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getDistance(
                                                           @RequestParam String address) {
        double[] coords1 = geocodingService.getLatLngFromAddress(address);
        log.warn("address1: " + address + String.format("%.2f %.2f", coords1[0], coords1[1]));
        boolean checkError = false;
        boolean checkError2 = false;
        if(coords1[0] == 0 && coords1[1] == 0) {
            checkError = true;
        }

        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();

        for(Restaurant restaurant : restaurantService.getAllRestaurants()) {
            log.warn("restaurantId: " + restaurant.getName());
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setId(restaurant.getId());
            restaurantDTO.setTitle(restaurant.getName());
            restaurantDTO.setDescription(restaurant.getDescription());
            restaurantDTO.setImages(restaurant.getImages());
            restaurantDTO.setOpen(restaurant.isOpen());
            restaurantDTO.setAddress(restaurant.getAddress());
            restaurantDTO.setRating(restaurant.getRating());
            restaurantDTO.setOpeningHours(restaurant.getOpeningHours());
            restaurantDTO.setReviewCount(restaurant.getReviewCount());
            restaurantDTO.setLat(restaurant.getLat());
            restaurantDTO.setLon(restaurant.getLon());
            String tmp = "";
            for(Food food : restaurant.getFoods()) {
                tmp = tmp + food.getName() + " ";
            }
            restaurantDTO.setFoods(tmp);
            if(restaurant.getAddress() != null && !checkError) {

                if(restaurant.getLat() == 0 && restaurant.getLon() == 0) {
                    checkError2 = true;
                    continue;
                }
                double[] res = distanceService.getDistanceAndTime(coords1[0], coords1[1], restaurant.getLat(), restaurant.getLon());
                restaurantDTO.setDistance(res[0]);
                restaurantDTO.setHours(res[1]*1.5);
                log.warn(String.format("%.2f %.2f %.2f %.2f", coords1[0], coords1[1], restaurant.getLat(), restaurant.getLon()));
            }
            if(checkError2 || checkError) {
                restaurantDTO.setDistance(8.0);
                restaurantDTO.setHours(25.0);
            }

            restaurantDTOS.add(restaurantDTO);
        }



//        if (coords1[0] == 0.0 || coords2[0] == 0.0) {
//            return new ResponseEntity<>("not found address", HttpStatus.NOT_FOUND);
//        }

//        String res = distanceService.getDistanceAndTime(coords1[0], coords1[1], coords2[0], coords2[1]);
        return new ResponseEntity<>(restaurantDTOS, HttpStatus.OK);
    }
}
