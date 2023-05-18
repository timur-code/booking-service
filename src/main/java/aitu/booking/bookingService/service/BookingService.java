package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.CartItemDTO;
import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.BookingItem;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.repository.BookingRepository;
import aitu.booking.bookingService.util.KeycloakUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.management.InstanceNotFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private MenuItemService menuItemService;
    private RestaurantService restaurantService;

    @Transactional
    public Booking createBooking(CreateBookingDTO bookingDTO, Authentication authentication) throws InstanceNotFoundException {
        UUID userUuid = KeycloakUtils.getUserUuidFromAuth(authentication);
        Booking booking = new Booking();
        booking.setUserUuid(userUuid);
        booking.setRestaurant(restaurantService.getRestaurantById(bookingDTO.getRestaurantId()));
        booking.setDtCreate(ZonedDateTime.now(ZoneId.of("Asia/Almaty")));
        booking.setTimeStart(bookingDTO.getTimeStart());
        if (!CollectionUtils.isEmpty(bookingDTO.getPreorder())) {
            List<BookingItem> menuItems = menuItemService.getMenuItemList(
                    bookingDTO.getPreorder().stream()
                            .map(CartItemDTO::getItemId)
                            .collect(Collectors.toList())
            ).stream().map(item -> BookingItem.builder().menuItem(item).build()).collect(Collectors.toList());
            booking.setMenuItemList(menuItems);
        }
        return bookingRepository.save(booking);
    }

//    @Transactional
//    public Booking addTempBooking(CreateBookingDTO bookingDTO, Authentication authentication) throws InstanceNotFoundException, IllegalAccessException {
//        Restaurant restaurant = restaurantService.getRestaurantById(bookingDTO.getRestaurantId());
//        UUID userUuid = KeycloakUtils.getUserUuidFromAuth(authentication);
//        if (bookingDTO.getId() == null) {
//            Optional<Booking> existingTempBooking = bookingRepository.findByUserUuidAndIsTempAndRestaurant(userUuid, true, restaurant);
//            existingTempBooking.ifPresent(booking -> bookingRepository.delete(booking));
//            return createTempBooking(bookingDTO, restaurant, userUuid);
//        } else {
//            return updateTempBooking(bookingDTO, userUuid);
//        }
//    }

//    private Booking createTempBooking(CreateBookingDTO bookingDTO, Restaurant restaurant, UUID userId) {
//        Booking booking = new Booking();
//        booking.setUserUuid(userId);
//        booking.setTimeStart(bookingDTO.getTimeStart());
//        booking.setDtCreate(ZonedDateTime.now(ZoneId.of("Asia/Almaty")));
//        booking.setRestaurant(restaurant);
//        booking.setIsActive(false);
//        booking.setIsTemp(true);
//        List<MenuItem> menuItems = menuItemService.getMenuItemList(bookingDTO.getPreorder());
//        booking.setMenuItemList(menuItems);
//        return bookingRepository.save(booking);
//    }
//
//    private Booking updateTempBooking(CreateBookingDTO bookingDTO, UUID userId)
//            throws InstanceNotFoundException, IllegalAccessException {
//        Booking booking = getBookingById(bookingDTO.getId());
//        if (!booking.getUserUuid().equals(userId)) {
//            throw new IllegalAccessException();
//        }
//        booking.setTimeStart(bookingDTO.getTimeStart());
//        List<MenuItem> menuItems = menuItemService.getMenuItemList(bookingDTO.getPreorder());
//        booking.setMenuItemList(menuItems);
//        return bookingRepository.save(booking);
//    }

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
