package com.quan.service.impl;

import com.quan.dto.RestaurantDTO;
import com.quan.dto.RestaurantUser;
import com.quan.model.Address;
import com.quan.model.Restaurant;
import com.quan.model.User;
import com.quan.repository.AddressRepository;
import com.quan.repository.RestaurantRepository;
import com.quan.repository.UserRepository;
import com.quan.request.CreateRestaurantRequest;
import com.quan.service.OSMGeocodingService;
import com.quan.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OSMGeocodingService osmGeocodingService;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {

        addressRepository.save(req.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(req.getAddress());
        restaurant.setName(req.getName());
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setDescription(req.getDescription());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setImages(req.getImages());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setOwner(user);
        restaurant.setRegistrationDate(LocalDateTime.now());

        Address address = req.getAddress();
        String address2 = address.getDetailAddress() + " " + address.getWard()
                + " " + address.getDistrict() + " " + address.getProvince();
        double[] coords = osmGeocodingService.getLatLngFromAddress(address2);
        restaurant.setLat(coords[0]);
        restaurant.setLon(coords[1]);

        return restaurantRepository.save(restaurant);

    }



    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest req) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        if(req.getCuisineType() != null){
            restaurant.setCuisineType(req.getCuisineType());
        }
        if(req.getDescription() != null){
            restaurant.setDescription(req.getDescription());
        }
        if(req.getContactInformation() != null){
            restaurant.setContactInformation(req.getContactInformation());
        }

        return restaurantRepository.save(restaurant);

    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.searchRestaurant(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        if(restaurant == null){
            throw new Exception("this restaurant does not exist");
        }
        return restaurant;
    }

    @Override
    public Restaurant findAllRestaurantByUserId(Long userId) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        return restaurant;
    }

    @Override
    public RestaurantUser addToFavorite(Long restaurantId, Long userId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        RestaurantUser restaurantDTO = new RestaurantUser();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setImages(restaurant.getImages());
        restaurantDTO.setDescription(restaurant.getDescription());
        restaurantDTO.setOpen(restaurant.isOpen());
        restaurantDTO.setTitle(restaurant.getName());
        restaurantDTO.setRating(restaurant.getRating());

        User user = userRepository.findById(userId).get();

        boolean isExist = false;

        for(RestaurantUser res : user.getFavorites()){
            if(res.getId().equals(restaurantDTO.getId())){
                isExist = true;
                break;
            }
        }

        if(isExist){
            user.getFavorites().removeIf(r -> r.getId().equals(restaurantDTO.getId()));
        }else{
            user.getFavorites().add(restaurantDTO);
        }
        userRepository.save(user);

        return restaurantDTO;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if(restaurant == null){
            throw new Exception("this restaurant does not exist");
        }
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
