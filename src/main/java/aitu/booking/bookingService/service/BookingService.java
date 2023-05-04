package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.model.*;
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
import java.util.*;

@Slf4j
@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private MenuItemService menuItemService;
    private RestaurantService restaurantService;

    @Transactional
    public Booking addTempBooking(CreateBookingDTO bookingDTO, Authentication authentication) throws InstanceNotFoundException, IllegalAccessException {
        Restaurant restaurant = restaurantService.getRestaurantById(bookingDTO.getRestaurantId());
        UUID userUuid = KeycloakUtils.getUserUuidFromAuth(authentication);
        if (bookingDTO.getId() == null) {
            Optional<Booking> existingTempBooking = bookingRepository.findByUserUuidAndIsTempAndRestaurant(userUuid, true, restaurant);
            existingTempBooking.ifPresent(booking -> bookingRepository.delete(booking));
            return createTempBooking(bookingDTO, restaurant, userUuid);
        } else {
            return updateTempBooking(bookingDTO, userUuid);
        }
    }

    private Booking createTempBooking(CreateBookingDTO bookingDTO, Restaurant restaurant, UUID userId) {
        Booking booking = new Booking();
        booking.setUserUuid(userId);
        booking.setTimeStart(bookingDTO.getTimeStart());
        booking.setDtCreate(ZonedDateTime.now(ZoneId.of("Asia/Almaty")));
        booking.setRestaurant(restaurant);
        booking.setIsActive(false);
        booking.setIsTemp(true);
        if (!CollectionUtils.isEmpty(bookingDTO.getPreorder())) {
            Order order = handleOrder(bookingDTO);
            //TODO: Add payment to order
        }
        return bookingRepository.save(booking);
    }

    private Booking updateTempBooking(CreateBookingDTO bookingDTO, UUID userId)
            throws InstanceNotFoundException, IllegalAccessException {
        Booking booking = getBookingById(bookingDTO.getId());
        if (!booking.getUserUuid().equals(userId)) {
            throw new IllegalAccessException();
        }
        booking.setTimeStart(bookingDTO.getTimeStart());
        if (!CollectionUtils.isEmpty(bookingDTO.getPreorder())) {
            Order order = handleOrder(bookingDTO);
            //TODO: Add payment to order
        } else if (booking.getOrder() != null) {
            booking.setOrder(null);
        }
        return bookingRepository.save(booking);
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

    private Order handleOrder(CreateBookingDTO bookingDTO) {
        Order order = new Order();
        Set<Long> itemIds = bookingDTO.getPreorder().keySet();
        List<MenuItem> menuItems = menuItemService.getMenuItemList(itemIds);
        List<OrderItem> orderItems = new ArrayList<>();
        menuItems.forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(item);
            orderItem.setAmount(bookingDTO.getPreorder().get(item.getId()));
            orderItems.add(orderItem);
        });
        order.setOrderItems(orderItems);
        return order;
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
