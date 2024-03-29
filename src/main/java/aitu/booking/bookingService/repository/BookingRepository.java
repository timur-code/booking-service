package aitu.booking.bookingService.repository;

import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByStripeSessionId(String sessionId);
    Page<Booking> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    Page<Booking> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.restaurant = :restaurant AND " +
            "(b.startTime BETWEEN :startTime AND :endTime OR b.endTime BETWEEN :startTime AND :endTime)")
    List<Booking> findBookingsByRestaurantAndTime(@Param("restaurant") Restaurant restaurant,
                                                  @Param("startTime") ZonedDateTime startTime,
                                                  @Param("endTime") ZonedDateTime endTime);
}
