package aitu.booking.bookingService.model;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "support_requests")
public class SupportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supportRequestsSeq")
    @SequenceGenerator(name = "supportRequestsSeq", sequenceName = "support_requests_seq", allocationSize = 1)
    private Long id;
    private UUID userId;
    private String phone;
    private Boolean isResolved;
    private String text;
    private ZonedDateTime dtCreate;
}
