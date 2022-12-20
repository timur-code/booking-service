package aitu.booking.bookingService.dto.create;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMenuDTO {
    private String name;
    private String description;
    private String language;
}
