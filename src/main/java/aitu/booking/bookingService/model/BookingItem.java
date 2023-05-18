package aitu.booking.bookingService.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "booking_items")
public class BookingItem extends BaseModel{
    @ManyToOne
    private MenuItem menuItem;
    private int quantity;

    public BookingItem() {

    }
}
