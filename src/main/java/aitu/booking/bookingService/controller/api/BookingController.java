package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.UserIdDTO;
import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@Slf4j
@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private BookingService bookingService;

    @Secured("ROLE_user")
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
    public ResponseEntity<ResponseSuccessWithData<Booking>> confirmBooking(@PathVariable Long id, @RequestBody UserIdDTO dto) {
        Booking booking = bookingService.confirmPayment(id, dto.getUserId());
        booking.setRestaurant(null);
        return ResponseEntity.ok(new ResponseSuccessWithData<>(booking));
    }

    @Secured("ROLE_user")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication authentication) {
        bookingService.cancelBooking(id, authentication);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_restaurant_admin", "ROLE_admin"})
    @PutMapping("/{id}/cancel/admin")
    public ResponseEntity<?> cancelBookingByAdmin(@PathVariable Long id) {
        bookingService.cancelBookingByAdmin(id);
        return ResponseEntity.ok().build();
    }


    @Secured("ROLE_user")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseSuccessWithData<Booking>> getBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(new ResponseSuccessWithData<>(booking));
    }

    @Secured("ROLE_user")
    @GetMapping
    public ResponseEntity<Page<Booking>> getBookings(@RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "250") int pageSize,
                                                     Authentication authentication) {
        Page<Booking> page = bookingService.getUserBookings(authentication, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    @Secured("ROLE_restaurant_admin")
    @GetMapping("/by-restaurant")
    public ResponseEntity<Page<Booking>> getBookings(@RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "250") int pageSize,
                                                     @RequestParam Long restaurantId,
                                                     Authentication authentication) {
        Page<Booking> page = bookingService.getRestaurantBookings(restaurantId, pageNum, pageSize, authentication);
        return ResponseEntity.ok(page);
    }

    @Secured("ROLE_admin")
    @GetMapping("/all")
    public ResponseEntity<Page<Booking>> getAllBookings(@RequestParam(defaultValue = "1") int pageNum,
                                                        @RequestParam(defaultValue = "250") int pageSize) {
        Page<Booking> page = bookingService.getAllBookings(pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
