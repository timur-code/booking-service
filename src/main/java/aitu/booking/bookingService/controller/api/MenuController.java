package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.AddItemToMenuDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.model.Menu;
import aitu.booking.bookingService.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@Secured("ROLE_restaurant_admin")
@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private MenuService menuService;

    @PostMapping("/item")
    public ResponseSuccessWithData<Menu> addItemToMenu(@RequestBody AddItemToMenuDTO dto) {
        try {
            Menu menu = menuService.addItemToMenu(dto);
            return new ResponseSuccessWithData<>(menu);
        } catch (InstanceNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }
}
