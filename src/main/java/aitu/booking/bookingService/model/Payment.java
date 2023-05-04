package aitu.booking.bookingService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseModel{
    private Long price;
    private ZonedDateTime dtCreated;
    private ZonedDateTime dtPayed;
}
