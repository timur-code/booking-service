package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.BookingItem;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.repository.BookingRepository;
import aitu.booking.bookingService.util.KeycloakUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService {
    private RestaurantService restaurantService;
    private MenuItemService menuItemService;
    private BookingRepository bookingRepository;

    public Booking createBooking(CreateBookingDTO createBookingDTO, Authentication authentication) throws InstanceNotFoundException {
        UUID userId = KeycloakUtils.getUserUuidFromAuth(authentication);
        Restaurant restaurant = restaurantService.getRestaurantById(createBookingDTO.getRestaurantId());

        ZonedDateTime startTime = createBookingDTO.getTimeStart();
        ZonedDateTime endTime = createBookingDTO.getTimeEnd();

        int totalGuests = bookingRepository
                .findBookingsByRestaurantAndTime(restaurant, startTime, endTime)
                .stream()
                .mapToInt(Booking::getGuests)
                .sum();

        if (totalGuests + createBookingDTO.getGuests() > restaurant.getSeats()) {
            throw new RuntimeException("Not enough seats available");
        }

        List<BookingItem> bookingItems = createBookingDTO.getPreorder().stream()
                .map(cartItemDTO -> {
                    try {
                        MenuItem item = menuItemService.getMenuItemById(cartItemDTO.getItemId());
                        return new BookingItem(item, cartItemDTO.getQuantity());
                    } catch (InstanceNotFoundException e) {
                        log.error("Error adding bookingItem to booking");
                    }
                    return null;
                })
                .collect(Collectors.toList());

        Booking booking = new Booking(restaurant, bookingItems, userId,
                startTime, endTime, createBookingDTO.getGuests());

        return bookingRepository.save(booking);
    }

    @Autowired
    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Autowired
    public void setMenuItemService(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Autowired
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
}
