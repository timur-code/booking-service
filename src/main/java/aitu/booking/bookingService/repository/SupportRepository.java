package aitu.booking.bookingService.repository;

import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.model.SupportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<SupportRequest, Long> {
}
