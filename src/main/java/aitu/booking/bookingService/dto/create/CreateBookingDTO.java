package aitu.booking.bookingService.dto.create;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateBookingDTO {
    private ZonedDateTime timeStart;
    private List<Long> preorder;
}
