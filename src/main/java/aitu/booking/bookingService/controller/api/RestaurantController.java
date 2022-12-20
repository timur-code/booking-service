package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.controller.BaseController;
import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.dto.create.CreateRestaurantDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@Secured("ROLE_user")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController extends BaseController {
    private RestaurantService restaurantService;

    @GetMapping("/{id}")
    public ResponseSuccessWithData<Restaurant> getRestaurant(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            return new ResponseSuccessWithData<>(restaurant);
        } catch (InstanceNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping("/{id}/booking")
    public ResponseSuccessWithData<Booking> bookRestaurant(@PathVariable Long id, @RequestBody CreateBookingDTO bookingDTO) {
        try {
            Booking booking = restaurantService.addBooking(id, bookingDTO);
            return new ResponseSuccessWithData<>(booking);
        } catch (InstanceNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping
    public ResponseSuccessWithData<Restaurant> createRestaurant(@RequestBody CreateRestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantService.addRestaurant(restaurantDTO);
        return new ResponseSuccessWithData<>(restaurant);
    }

    @PostMapping("/{id}/menu")
    public ResponseSuccessWithData<Restaurant> addMenu(@PathVariable Long id, @RequestBody CreateMenuDTO menuDTO) {
        try {
            Restaurant restaurant = restaurantService.addMenu(id, menuDTO);
            return new ResponseSuccessWithData<>(restaurant);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
}
