package aitu.booking.bookingService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_admins")
public class RestaurantAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurantAdminsSeq")
    @SequenceGenerator(name = "restaurantAdminsSeq", sequenceName = "restaurant_admins_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    private UUID userId;
    private String phone;
}
