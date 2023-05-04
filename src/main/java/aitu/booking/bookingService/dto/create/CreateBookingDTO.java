package aitu.booking.bookingService.dto.create;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CreateBookingDTO {
    private Long id;
    private Long restaurantId;
    private ZonedDateTime timeStart;
    private Map<Long, Integer> preorder; //Long is id of the menuItem, Integer is the amount - in entity OrderItem
}
