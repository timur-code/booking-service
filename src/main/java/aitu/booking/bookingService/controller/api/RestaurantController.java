package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.controller.BaseController;
import aitu.booking.bookingService.dto.RequestIdsDTO;
import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.dto.create.CreateRestaurantDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.dto.restaurant.RestaurantSmallDTO;
import aitu.booking.bookingService.exception.ApiException;
import aitu.booking.bookingService.model.Menu;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Slf4j
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
    public ResponseEntity<ResponseSuccessWithData<RestaurantSmallDTO>> getRestaurant(@PathVariable Long id) {
        try {
            RestaurantSmallDTO restaurant = restaurantService.getRestaurantSmallById(id);
            return ResponseEntity.ok(new ResponseSuccessWithData<>(restaurant));
        } catch (InstanceNotFoundException ex) {
            log.error("Error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/list-by-id")
    public ResponseEntity<ResponseSuccessWithData<List<RestaurantSmallDTO>>> getRestaurants(@RequestBody RequestIdsDTO dto) {
        return ResponseEntity.ok(new ResponseSuccessWithData<>(restaurantService.getRestaurantsSmallByIds(dto.getIdList())));
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<ResponseSuccessWithData<Menu>> getRestaurantMenu(@PathVariable Long id) {
        try {
            Menu menu = restaurantService.getRestaurantMenu(id);
            return ResponseEntity.ok(new ResponseSuccessWithData<>(menu));
        } catch (InstanceNotFoundException | IndexOutOfBoundsException ex) {
            log.error("Error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Secured({"ROLE_restaurant_admin"})
    @GetMapping("/my-restaurant")
    public ResponseEntity<RestaurantSmallDTO> getMyRestaurant(Authentication authentication) {
        try {
            RestaurantSmallDTO dto = restaurantService.getMyRestaurant(authentication);
            return ResponseEntity.ok(dto);
        } catch (IllegalAccessException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InstanceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({"ROLE_admin"})
    @PostMapping
    public ResponseSuccessWithData<Restaurant> createRestaurant(@RequestBody CreateRestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantService.addRestaurant(restaurantDTO);
        return new ResponseSuccessWithData<>(restaurant);
    }

    @Autowired
    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
}
