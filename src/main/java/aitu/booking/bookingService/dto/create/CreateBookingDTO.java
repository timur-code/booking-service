package aitu.booking.bookingService.dto.create;

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
    private List<Long> preorder;
}
