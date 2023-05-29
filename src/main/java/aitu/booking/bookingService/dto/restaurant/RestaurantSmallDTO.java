package aitu.booking.bookingService.dto.restaurant;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RestaurantSmallDTO {
    private Long id;
    private String name;
    private String description;
    private Integer seats;
    private String image;
}
