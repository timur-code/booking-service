package aitu.booking.bookingService.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class RestaurantAdminDTO {

    private UUID id;
    private Long restaurantId;
    private String phone;
    private String password;
}
