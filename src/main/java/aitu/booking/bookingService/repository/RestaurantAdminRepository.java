package aitu.booking.bookingService.repository;

import aitu.booking.bookingService.model.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
}
