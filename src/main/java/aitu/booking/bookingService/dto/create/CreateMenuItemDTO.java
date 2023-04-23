package aitu.booking.bookingService.dto.create;

import lombok.Builder;
import lombok.Data;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@Builder
public class CreateMenuItemDTO {
    private Long menuId;
    private String name;
    private String description;
    @ElementCollection
    private List<String> images;
}
