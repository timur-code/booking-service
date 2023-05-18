package aitu.booking.bookingService.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "support_requests")
public class SupportRequest extends BaseModel {
    private UUID userId;
    private String text;
    private ZonedDateTime dtCreate;
}
