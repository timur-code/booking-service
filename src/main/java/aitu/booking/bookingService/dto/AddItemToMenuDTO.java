package aitu.booking.bookingService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddItemToMenuDTO {
    private Long itemId;
    private Long menuId;
}
