package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.CartItemDTO;
import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.exception.ApiException;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.BookingItem;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.repository.BookingItemRepository;
import aitu.booking.bookingService.repository.BookingRepository;
import aitu.booking.bookingService.util.KeycloakUtils;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.management.InstanceNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BookingService {
    private RestaurantService restaurantService;
    private MenuItemService menuItemService;
    private StripeService stripeService;
    private BookingRepository bookingRepository;
    private BookingItemRepository bookingItemRepository;

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
            throw new ApiException(400, "Not enough seats available");
        }

        String sessionId = null;
        Booking booking = null;
        if (!CollectionUtils.isEmpty(createBookingDTO.getPreorder())) {
            List<BookingItem> bookingItems = saveBookingItems(createBookingDTO.getPreorder());
            // This line communicates with Stripe API to create a Checkout Session
            booking = new Booking(restaurant, userId, startTime, endTime,
                    createBookingDTO.getGuests());
            booking = bookingRepository.save(booking);
            try {
                sessionId = stripeService.createCheckoutSession(booking.getId(), createBookingDTO.getPreorder());
                log.info("Created Stripe Checkout Session: {}", sessionId);
            } catch (StripeException ex) {
                log.error("Stripe error during booking of user {}: {}", userId, ex);
                throw new ApiException(500, "stripe.error");
            }
            booking.setBookingItems(bookingItems);
            booking.setStripeSessionId(sessionId);
            booking.setPayed(false);
        } else {
            booking = new Booking(restaurant, userId, startTime, endTime,
                    createBookingDTO.getGuests());
        }


        return bookingRepository.save(booking);
    }

    public Booking confirmPayment(Long bookingId, Authentication authentication) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(404, "booking.not-found"));

        UUID userId = KeycloakUtils.getUserUuidFromAuth(authentication);
        if (booking.getUserId().equals(userId)) {
            throw new ApiException(401, "user.no-access");
        }
        booking.setPayed(true);
        return bookingRepository.save(booking);
    }

    public Page<Booking> getUserBookings(Authentication authentication, int pageNum, int pageSize) {
        UUID userId = KeycloakUtils.getUserUuidFromAuth(authentication);
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(--pageNum, pageSize));
    }

    public Page<Booking> getRestaurantBookings(Long restaurantId, int pageNum, int pageSize, Authentication authentication) {
        restaurantService.verifyAccess(authentication, restaurantId);
        return bookingRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, PageRequest.of(--pageNum, pageSize));
    }

    public Page<Booking> getAllBookings(int pageNum, int pageSize) {
        return bookingRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(--pageNum, pageSize));
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public void cancelBooking(Long id, Authentication authentication) {
        Booking booking = getBookingById(id);
        UUID userId = KeycloakUtils.getUserUuidFromAuth(authentication);
        if (!booking.getUserId().equals(userId)) {
            throw new ApiException(403, "user.no-access");
        }
        booking.cancelBooking();
        bookingRepository.save(booking);
    }

    private List<BookingItem> saveBookingItems(List<CartItemDTO> preorder) {
        List<BookingItem> bookingItems = preorder.stream()
                .map(cartItemDTO -> {
                    try {
                        MenuItem item = menuItemService.getMenuItemById(cartItemDTO.getItemId());
                        return new BookingItem(item, cartItemDTO.getQuantity());
                    } catch (InstanceNotFoundException e) {
                        log.error("Error adding bookingItem to booking");
                    }
                    return null;
                })
                .toList();

        return bookingItemRepository.saveAll(bookingItems);
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
    public void setStripeService(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Autowired
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Autowired
    public void setBookingItemRepository(BookingItemRepository bookingItemRepository) {
        this.bookingItemRepository = bookingItemRepository;
    }
}
