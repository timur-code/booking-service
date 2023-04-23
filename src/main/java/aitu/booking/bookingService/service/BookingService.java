package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.repository.BookingRepository;
import aitu.booking.bookingService.util.KeycloakUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private MenuItemService menuItemService;
    private RestaurantService restaurantService;

    @Transactional
    public void addTempBooking(CreateBookingDTO bookingDTO, Authentication authentication) throws InstanceNotFoundException {
        Restaurant restaurant = restaurantService.getRestaurantById(bookingDTO.getRestaurantId());
        UUID userUuid = KeycloakUtils.getUserUuidFromAuth(authentication);
        Optional<Booking> existingTempBooking = bookingRepository.findByUserUuidAndIsTempAndRestaurant(userUuid, true, restaurant);
        existingTempBooking.ifPresent(booking -> bookingRepository.delete(booking));
        createTempBooking(bookingDTO, restaurant, userUuid);
    }

    private void createTempBooking(CreateBookingDTO bookingDTO, Restaurant restaurant, UUID userId) {
        Booking booking = new Booking();
        booking.setUserUuid(userId);
        booking.setTimeStart(bookingDTO.getTimeStart());
        booking.setDtCreate(ZonedDateTime.now(ZoneId.of("Asia/Almaty")));
        booking.setRestaurant(restaurant);
        booking.setIsActive(false);
        booking.setIsTemp(true);
        List<MenuItem> menuItems = menuItemService.getMenuItemList(bookingDTO.getPreorder());
        booking.setMenuItemList(menuItems);
        bookingRepository.save(booking);
    }

    @Transactional
    public void confirmBooking(Long id, Authentication authentication) throws InstanceNotFoundException,
            IllegalAccessException, IllegalArgumentException {
        UUID userUuid = KeycloakUtils.getUserUuidFromAuth(authentication);
        Booking booking = getBookingById(id);
        if (!userUuid.equals(booking.getUserUuid())) {
            throw new IllegalAccessException();
        }

        if (!booking.getIsActive() && booking.getIsTemp() &&
                booking.getTimeStart().isAfter(ZonedDateTime.now(ZoneId.of("Asia/Almaty")))) {
            booking.setIsTemp(false);
            booking.setIsActive(true);
        } else {
            throw new IllegalArgumentException();
        }
        bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) throws InstanceNotFoundException {
        return bookingRepository.findById(id)
                .orElseThrow(InstanceNotFoundException::new);
    }

    @Autowired
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Autowired
    public void setMenuItemService(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Autowired
    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
}
