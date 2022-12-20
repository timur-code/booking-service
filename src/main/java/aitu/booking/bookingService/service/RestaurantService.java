package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.dto.create.CreateRestaurantDTO;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.Menu;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;

@Service
public class RestaurantService extends BaseService {
    private RestaurantRepository restaurantRepository;
    private MenuService menuService;
    private MenuItemService menuItemService;
    private BookingService bookingService;

    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) throws InstanceNotFoundException {
        return restaurantRepository.findById(id)
                .orElseThrow(InstanceNotFoundException::new);
    }

    @Transactional
    public Restaurant addRestaurant(CreateRestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public Restaurant addMenu(Long restaurantId, CreateMenuDTO menuDTO) throws InstanceNotFoundException {
        Menu menu = menuService.createMenu(menuDTO);
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurant.addMenu(menu);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public Booking addBooking(Long restaurantId, CreateBookingDTO bookingDTO) throws InstanceNotFoundException {
        Restaurant restaurant = getRestaurantById(restaurantId);
        Booking booking = bookingService.createBooking(bookingDTO);
        restaurant.addBooking(booking);
        restaurantRepository.save(restaurant);
        return booking;
    }

    @Autowired
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setMenuItemService(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
