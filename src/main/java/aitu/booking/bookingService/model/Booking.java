package aitu.booking.bookingService.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookingsSeq")
    @SequenceGenerator(name = "bookingsSeq", sequenceName = "bookings_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    @ManyToOne
    private Restaurant restaurant;
    @OneToMany
    @JoinTable(name = "booking_booking_item",
            joinColumns = @JoinColumn(
                    name = "booking_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "booking_item_id",
                    referencedColumnName = "id"
            ))
    private List<BookingItem> bookingItems;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private ZonedDateTime startTime;
    @Column(nullable = false)
    private ZonedDateTime endTime;
    @Column(nullable = false)
    private Integer guests;
    @CreationTimestamp
    private ZonedDateTime createdAt;
    private Boolean isCanceled = false;
    private Boolean isPayed;
    private String stripeSessionId;

    public Booking() {
    }

    public Booking(Restaurant restaurant, List<BookingItem> bookingItems, UUID userId,
                   ZonedDateTime startTime, ZonedDateTime endTime, Integer guests,
                   ZonedDateTime createdAt, Boolean isCanceled, Boolean isPayed,
                   String stripeSessionId) {
        this.restaurant = restaurant;
        this.bookingItems = bookingItems;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.guests = guests;
        this.createdAt = createdAt;
        this.isCanceled = isCanceled;
        this.isPayed = isPayed;
        this.stripeSessionId = stripeSessionId;
    }

    public Booking(Restaurant restaurant, List<BookingItem> bookingItems,
                   UUID userId, ZonedDateTime startTime,
                   ZonedDateTime endTime, Integer guests,
                   String stripeSessionId) {
        this.restaurant = restaurant;
        this.bookingItems = bookingItems;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.guests = guests;
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
        this.stripeSessionId = stripeSessionId;
    }

    public Booking(Restaurant restaurant, UUID userId,
                   ZonedDateTime startTime, ZonedDateTime endTime,
                   Integer guests) {
        this.restaurant = restaurant;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.guests = guests;
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addBookingItem(BookingItem bookingItem) {
        if (this.bookingItems == null) {
            this.bookingItems = new ArrayList<>();
        }
        this.bookingItems.add(bookingItem);
    }

    public void cancelBooking() {
        this.isCanceled = true;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<BookingItem> getBookingItems() {
        return bookingItems;
    }

    public void setBookingItems(List<BookingItem> bookingItems) {
        this.bookingItems = bookingItems;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }

    public String getStripeSessionId() {
        return stripeSessionId;
    }

    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }
}
