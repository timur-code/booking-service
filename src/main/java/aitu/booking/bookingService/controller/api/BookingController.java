package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@Slf4j
@Secured("ROLE_user")
@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<ResponseSuccessWithData<Booking>> bookRestaurant(@RequestBody CreateBookingDTO bookingDTO,
                                                                           Authentication authentication) {
        try {
            Booking booking = bookingService.createBooking(bookingDTO, authentication);
            booking.setRestaurant(null);
            return ResponseEntity.ok(new ResponseSuccessWithData<>(booking));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ResponseSuccessWithData<Booking>> confirmBooking(@PathVariable Long id, Authentication authentication) {
        Booking booking = bookingService.confirmPayment(id, authentication);
        booking.setRestaurant(null);
        return ResponseEntity.ok(new ResponseSuccessWithData<>(booking));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication authentication) {
        bookingService.cancelBooking(id, authentication);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseSuccessWithData<Booking>> getBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(new ResponseSuccessWithData<>(booking));
    }

    @GetMapping
    public ResponseEntity<Page<Booking>> getBookings(@RequestParam int pageNum,
                                                     @RequestParam int pageSize,
                                                     Authentication authentication) {
        Page<Booking> page = bookingService.getUserBookings(authentication, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    @Secured("ROLE_restaurant_admin")
    @GetMapping("/by-restaurant")
    public ResponseEntity<Page<Booking>> getBookings(@RequestParam int pageNum,
                                                     @RequestParam int pageSize,
                                                     @RequestParam Long restaurantId,
                                                     Authentication authentication) {
        Page<Booking> page = bookingService.getRestaurantBookings(restaurantId, pageNum, pageSize, authentication);
        return ResponseEntity.ok(page);
    }

    @Secured("ROLE_admin")
    @GetMapping("/all")
    public ResponseEntity<Page<Booking>> getAllBookings(@RequestParam int pageNum,
                                                        @RequestParam int pageSize) {
        Page<Booking> page = bookingService.getAllBookings(pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
