package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.create.CreateMenuItemDTO;
import aitu.booking.bookingService.dto.responses.ResponseFail;
import aitu.booking.bookingService.dto.responses.ResponseSuccess;
import aitu.booking.bookingService.dto.responses.StatusResponse;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.service.MenuItemService;
import aitu.booking.bookingService.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;


@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private MenuService menuService;
    private MenuItemService menuItemService;

    @Secured("ROLE_restaurant_admin")
    @PostMapping("/item")
    public ResponseEntity<StatusResponse> addItemToMenu(@RequestBody CreateMenuItemDTO dto) {
        try {
            menuService.addItemToMenu(dto);
            return ResponseEntity.ok(new ResponseSuccess());
        } catch (InstanceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ResponseFail(ex.getMessage()));
        }
    }

    @Secured("ROLE_user")
    @GetMapping("/item/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(menuItemService.getMenuItemById(id));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setMenuItemService(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
}
