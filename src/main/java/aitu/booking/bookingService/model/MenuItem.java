package aitu.booking.bookingService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
public class MenuItem extends BaseModel {
    private String name;
    private String description;
    @ElementCollection
    private List<String> images;

    public void addImage(String image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
    }
}
