package aitu.booking.bookingService.controller.api;

import aitu.booking.bookingService.dto.create.CreateMenuItemDTO;
import aitu.booking.bookingService.dto.responses.ResponseFail;
import aitu.booking.bookingService.dto.responses.ResponseSuccess;
import aitu.booking.bookingService.dto.responses.StatusResponse;
import aitu.booking.bookingService.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceNotFoundException;

@Secured("ROLE_restaurant_admin")
@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private MenuService menuService;

    @PostMapping("/item")
    public ResponseEntity<StatusResponse> addItemToMenu(@RequestBody CreateMenuItemDTO dto) {
        try {
            menuService.addItemToMenu(dto);
            return ResponseEntity.ok(new ResponseSuccess());
        } catch (InstanceNotFoundException ex) {
            return ResponseEntity.badRequest().body(new ResponseFail(ex.getMessage()));
        }
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }
}
