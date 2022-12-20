package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.create.CreateMenuItemDTO;
import aitu.booking.bookingService.dto.responses.ResponseSuccessWithData;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured("ROLE_user")
@RestController
@RequestMapping("/api/menu_item")
public class MenuItemController {
    private MenuItemService menuItemService;

    @PostMapping
    public ResponseSuccessWithData<MenuItem> addMenuItem(@RequestBody CreateMenuItemDTO dto) {
        MenuItem menuItem = menuItemService.createMenuItem(dto);
        return new ResponseSuccessWithData<>(menuItem);
    }

    @Autowired
    public void setMenuItemService(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
}
