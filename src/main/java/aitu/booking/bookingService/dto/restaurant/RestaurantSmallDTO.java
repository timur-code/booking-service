package aitu.booking.bookingService.dto.restaurant;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Builder
@Data
public class RestaurantSmallDTO {
    private Long id;
    private String name;
    private String description;
    private Integer seats;
    private String location;
    private LocalTime timeOpen;
    private LocalTime timeClosed;
    private String image;
}
