package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccess;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.exception.ApiException;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
