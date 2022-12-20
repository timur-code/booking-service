package aitu.booking.bookingService.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
public class Menu extends BaseModel {
    private String name;
    private String description;
    private String language;
    @OneToMany
    private List<MenuItem> menuItems;

    public void addMenuItem(MenuItem menuItem) {
        if (menuItems == null) {
            menuItems = new ArrayList<>();
        }
        if (!menuItems.contains(menuItem)) {
            menuItems.add(menuItem);
        }
    }
}
