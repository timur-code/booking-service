package aitu.booking.bookingService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
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
    @OneToOne
    private RestaurantAdmin admin;


    public void addMenu(Menu menu) {
        if (menuList == null) {
            menuList = new ArrayList<>();
        }
        menuList.add(menu);
    }
}
