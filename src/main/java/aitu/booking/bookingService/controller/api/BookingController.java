package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccess;
import aitu.booking.bookingService.exception.ApiException;
import aitu.booking.bookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@Secured("ROLE_user")
@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<ResponseSuccess> bookRestaurant(@RequestBody CreateBookingDTO bookingDTO,
                                                          Authentication authentication) {
        try {
            bookingService.addTempBooking(bookingDTO, authentication);
            return ResponseEntity.ok(new ResponseSuccess());
        } catch (InstanceNotFoundException e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ResponseSuccess> confirmBooking(@PathVariable Long id,
                                                          Authentication authentication) {
        try {
            bookingService.confirmBooking(id, authentication);
            return ResponseEntity.ok(new ResponseSuccess());
        } catch (InstanceNotFoundException | IllegalArgumentException e) {
            throw new ApiException(400, e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ApiException(403, e.getMessage());
        }
    }

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
