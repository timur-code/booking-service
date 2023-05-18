package aitu.booking.bookingService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@ToString
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking extends BaseModel{
    private UUID userUuid;
    @ManyToOne
    private Restaurant restaurant;
    private ZonedDateTime timeStart;
    private ZonedDateTime dtCreate;
    @ManyToMany
    private List<MenuItem> menuItemList;
}
