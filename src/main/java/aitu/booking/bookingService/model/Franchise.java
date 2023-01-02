package aitu.booking.bookingService.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Table(name = "franchises")
@Getter
@Setter
@NoArgsConstructor
public class Franchise extends BaseModel {
    private String name;
    private String userUuid;
    @OneToMany
    private List<Restaurant> restaurantList;

    public void addRestaurant(Restaurant restaurant) {
        if (restaurantList == null) {
            restaurantList = new ArrayList<>();
        }
        restaurantList.add(restaurant);
    }
}
