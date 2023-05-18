package aitu.booking.bookingService.dto.create;

import aitu.booking.bookingService.dto.CartItemDTO;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class CreateBookingDTO {
    private Long id;
    private Long restaurantId;
    private ZonedDateTime timeStart;
    private List<CartItemDTO> preorder;
}
