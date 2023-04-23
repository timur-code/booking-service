package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.controller.BaseController;
import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.dto.create.CreateRestaurantDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.dto.restaurant.RestaurantSmallDTO;
import aitu.booking.bookingService.exception.ApiException;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.Menu;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController extends BaseController {
    private RestaurantService restaurantService;

    @GetMapping("/list")
    public Page<RestaurantSmallDTO> list(@RequestParam(required = false, defaultValue = "1") int page,
                                         @RequestParam(required = false, defaultValue = "100") int size) {
        return restaurantService.list(page, size);
    }


    @GetMapping("/{id}")
    public ResponseSuccessWithData<RestaurantSmallDTO> getRestaurant(@PathVariable Long id) {
        try {
            RestaurantSmallDTO restaurant = restaurantService.getRestaurantSmallById(id);
            return new ResponseSuccessWithData<>(restaurant);
        } catch (InstanceNotFoundException ex) {
            throw new ApiException("404", ex.getMessage());
        }
    }

    @GetMapping("/{id}/menu")
    public ResponseSuccessWithData<Menu> getRestaurantMenu(@PathVariable Long id) {
        try {
            Menu menu = restaurantService.getRestaurantMenu(id);
            return new ResponseSuccessWithData<>(menu);
        } catch (InstanceNotFoundException | IndexOutOfBoundsException ex) {
            throw new ApiException("404", ex.getMessage());
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
            throw new ApiException("404");
        }
    }

    @Autowired
    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
}
