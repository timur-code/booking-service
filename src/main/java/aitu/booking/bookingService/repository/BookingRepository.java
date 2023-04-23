package aitu.booking.bookingService.repository;

import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByUserUuidAndIsTempAndRestaurant(UUID userUuid, Boolean isTemp, Restaurant restaurant);
}
