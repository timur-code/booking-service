package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.AddItemToMenuDTO;
import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.model.Menu;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.management.InstanceNotFoundException;

@Service
public class MenuService extends BaseService {
    private MenuItemService menuItemService;
    private MenuRepository menuRepository;

    @PostConstruct
    protected void init() {
        super.init();
    }

    @Transactional
    public Menu createMenu(CreateMenuDTO dto) {
        Menu menu = mapDtoToMenu(dto);
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu addItemToMenu(AddItemToMenuDTO dto) throws InstanceNotFoundException {
        MenuItem menuItem = menuItemService.getMenuItemById(dto.getItemId());
        Menu menu = getMenu(dto.getMenuId());
        menu.addMenuItem(menuItem);
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public Menu getMenu(Long id) throws InstanceNotFoundException {
        return menuRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
    }


    private Menu mapDtoToMenu(CreateMenuDTO dto) {
        return mapper.convertValue(dto, Menu.class);
    }


    @Autowired
    public void setMenuItemService(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
}
