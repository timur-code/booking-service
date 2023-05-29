package aitu.booking.bookingService.dto.create;

import lombok.Builder;
import lombok.Data;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@Builder
public class CreateMenuItemDTO {
    private Long restaurantId;
    private String name;
    private Integer price;
    private String description;
    private List<String> images;
}
