package aitu.booking.bookingService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
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
    private LocalDateTime timeStart;
    @ManyToMany
    private List<MenuItem> menuItemList;
}
