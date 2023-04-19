package aitu.booking.bookingService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends BaseModel {
    private String name;
    private String description;
    @OneToMany
    private List<Menu> menuList;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Booking> bookings;


    public void addMenu(Menu menu) {
        if (menuList == null) {
            menuList = new ArrayList<>();
        }
        menuList.add(menu);
    }

    public void addBooking(Booking booking) {
        if (bookings == null) {
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
    }
}
