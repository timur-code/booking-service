package aitu.booking.bookingService.dto.create;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class CreateRestaurantDTO {
    private String name;
    private String description;
    private Integer seats;
    private String image;
    private String location;
    private LocalTime timeOpen;
    private LocalTime timeClosed;
    private String adminPhone;
    private String password;
}
