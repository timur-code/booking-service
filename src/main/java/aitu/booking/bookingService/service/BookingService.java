package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.create.CreateBookingDTO;
import aitu.booking.bookingService.model.Booking;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.repository.BookingRepository;
import aitu.booking.bookingService.util.KeycloakUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private MenuItemService menuItemService;

    @Transactional
    public Booking createBooking(CreateBookingDTO bookingDTO, UUID userId) {
        Booking booking = new Booking();
        booking.setUserUuid(userId);
        booking.setTimeStart(bookingDTO.getTimeStart());
        booking.setIsActive(true);
        List<MenuItem> menuItems = menuItemService.getMenuItemList(bookingDTO.getPreorder());
        booking.setMenuItemList(menuItems);
        return bookingRepository.save(booking);
    }

    @Autowired
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Autowired
    public void setMenuItemService(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
}
