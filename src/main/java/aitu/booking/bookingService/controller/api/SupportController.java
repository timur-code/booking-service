package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.SupportRequestDTO;
import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccess;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.SupportRequest;
import aitu.booking.bookingService.service.BookingService;
import aitu.booking.bookingService.service.SupportService;
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
@RestController
@RequestMapping("/api/support")
public class SupportController {
    private SupportService supportService;

    @Secured("ROLE_admin")
    @GetMapping
    public ResponseEntity<Page<SupportRequest>> getPageOfRequests() {
        return ResponseEntity.ok(supportService.getPageOfRequests());
    }

    @Secured("ROLE_admin")
    @PostMapping("/{id}/resolve")
    public ResponseEntity<ResponseSuccess> resolve(@PathVariable Long id) {
        try {
            supportService.resolveRequest(id);
            return ResponseEntity.ok(new ResponseSuccess());
        } catch (InstanceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured("ROLE_user")
    @PostMapping
    public ResponseEntity<ResponseSuccess> createRequest(@RequestBody SupportRequestDTO dto,
                                           Authentication authentication) {
        supportService.createRequest(dto, authentication);
        return ResponseEntity.ok(new ResponseSuccess());
    }

    @Autowired
    public void setSupportService(SupportService supportService) {
        this.supportService = supportService;
    }
}
