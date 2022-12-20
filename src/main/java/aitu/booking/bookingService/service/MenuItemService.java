package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.create.CreateMenuItemDTO;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class MenuItemService extends BaseService {
    private MenuItemRepository itemRepository;

    @PostConstruct
    protected void init() {
        super.init();
    }

    @Transactional
    public MenuItem createMenuItem(CreateMenuItemDTO dto) {
        MenuItem menuItem = mapper.convertValue(dto, MenuItem.class);
        return itemRepository.save(menuItem);
    }

    public MenuItem getMenuItemById(Long id) throws InstanceNotFoundException {
        return itemRepository.findById(id)
                .orElseThrow(InstanceNotFoundException::new);
    }

    public List<MenuItem> getMenuItemList(List<Long> itemIds) {
        return itemRepository.findAllById(itemIds);
    }

    private MenuItem mapDtoToMenuItem(CreateMenuItemDTO dto) {
        return mapper.convertValue(dto, MenuItem.class);
    }

    @Autowired
    public void setItemRepository(MenuItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
