package aitu.booking.bookingService.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Builder
@Entity
@Table(name = "booking_items")
public class BookingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookingItemsSeq")
    @SequenceGenerator(name = "bookingItemsSeq", sequenceName = "booking_items_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    @ManyToOne
    private MenuItem menuItem;
    private int quantity;

    public BookingItem() {

    }

    public BookingItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public BookingItem(Long id, MenuItem menuItem, int quantity) {
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }
}
