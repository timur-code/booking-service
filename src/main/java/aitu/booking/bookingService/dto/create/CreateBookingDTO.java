package aitu.booking.bookingService.dto.create;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateBookingDTO {
    private UUID userUuid;
    private LocalDateTime timeStart;
    private List<Long> preorder;
}
