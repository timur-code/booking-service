package aitu.booking.bookingService.service;

import aitu.booking.bookingService.BaseTest;
import aitu.booking.bookingService.dto.AddItemToMenuDTO;
import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.model.Menu;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MenuServiceTest extends BaseTest {
    private MenuService menuService;

    @Test
    void createMenu() {
        CreateMenuDTO dto = getCreateMenuDTO();
        Menu menu = menuService.createMenu(dto);
        log.info("Menu: {}", menu);
        assertNotNull(menu);
        assertNotNull(menu.getId());
    }

    @Test
    @SneakyThrows
    @Transactional
    @Rollback(value = false)
    void addItemToMenu() {
        AddItemToMenuDTO dto = AddItemToMenuDTO.builder()
                .itemId(2L)
                .menuId(1L)
                .build();
//        menuService.addItemToMenu(dto);
        Menu menu = menuService.getMenu(1L);

        log.info("Menu items: {}", menu.getMenuItems());
        assertNotNull(menu.getMenuItems());
        assertFalse(menu.getMenuItems().isEmpty());
    }

    @Test
    void getMenu() {
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }
}
