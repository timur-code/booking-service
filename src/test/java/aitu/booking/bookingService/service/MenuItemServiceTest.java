package aitu.booking.bookingService.service;

import aitu.booking.bookingService.BaseTest;
import aitu.booking.bookingService.dto.create.CreateMenuItemDTO;
import aitu.booking.bookingService.model.MenuItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Slf4j
class MenuItemServiceTest extends BaseTest {

    private MenuItemService service;

    @Test
    void createMenuItem() {
        CreateMenuItemDTO dto = getCreateMenuItemDTO();
        MenuItem item = service.createMenuItem(dto);
        log.info("Item: {}", item);
        assertNotNull(item);
        assertNotNull(item.getId());
        assertEquals(dto.getName(), item.getName());
    }

    @Autowired
    public void setService(MenuItemService service) {
        this.service = service;
    }
}
