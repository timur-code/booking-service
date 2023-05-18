package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.CartItemDTO;
import aitu.booking.bookingService.dto.create.CreateMenuItemDTO;
import aitu.booking.bookingService.model.BookingItem;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<BookingItem> getBookingItems(List<CartItemDTO> preorder) {
        return getMenuItemList(preorder.stream().map(CartItemDTO::getItemId).collect(Collectors.toList()))
                .stream().map(item -> new BookingItem(item, preorder.stream().filter(el -> el.getItemId().equals(item.getId())).findFirst().get().getQuantity()))
                .collect(Collectors.toList());
    }

    private MenuItem mapDtoToMenuItem(CreateMenuItemDTO dto) {
        return mapper.convertValue(dto, MenuItem.class);
    }

    @Autowired
    public void setItemRepository(MenuItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
