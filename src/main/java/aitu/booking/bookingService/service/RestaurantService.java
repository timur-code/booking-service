package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.dto.create.CreateRestaurantDTO;
import aitu.booking.bookingService.dto.restaurant.RestaurantSmallDTO;
import aitu.booking.bookingService.exception.ApiException;
import aitu.booking.bookingService.model.Menu;
import aitu.booking.bookingService.model.Restaurant;
import aitu.booking.bookingService.model.RestaurantAdmin;
import aitu.booking.bookingService.repository.RestaurantRepository;
import aitu.booking.bookingService.util.KeycloakUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

@Slf4j
@Service
public class RestaurantService extends BaseService {
    private RestaurantRepository restaurantRepository;
    private MenuService menuService;
    private RestaurantAdminService restaurantAdminService;

    public Page<RestaurantSmallDTO> list(int page, int size) {
        return restaurantRepository.findAll(PageRequest.of(--page, size))
                .map(this::toSmallDTO);
    }

    public Menu getRestaurantMenu(Long id) throws InstanceNotFoundException, IndexOutOfBoundsException {
        return getRestaurantById(id).getMenuList().get(0);
    }

    public RestaurantSmallDTO getRestaurantSmallById(Long id) throws InstanceNotFoundException {
        return toSmallDTO(getRestaurantById(id));
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) throws InstanceNotFoundException {
        return restaurantRepository.findById(id)
                .orElseThrow(InstanceNotFoundException::new);
    }

    @Transactional
    public Restaurant addRestaurant(CreateRestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setSeats(restaurantDTO.getSeats());
        restaurant.setImage(restaurantDTO.getImage());
        Restaurant res = restaurantRepository.save(restaurant);

        try {
            addMenu(restaurant.getId(),
                    CreateMenuDTO.builder()
                            .description(restaurantDTO.getDescription())
                            .name(restaurantDTO.getName())
                            .language("ru")
                            .build());
        } catch (InstanceNotFoundException e) {
            log.error("Error in transaction. Error adding menu to a new restaurant");
        }

        RestaurantAdmin admin;
        try {
            admin = restaurantAdminService.createAdmin(res.getId(), restaurantDTO.getAdminPhone(), restaurantDTO.getPassword());
            res.setAdmin(admin);
        } catch (JsonProcessingException ex) {
            log.error("Error in transaction. Error adding admin to a new restaurant:\n", ex);
            throw new ApiException(500, "admin.create.json.error");
        }

        return restaurantRepository.save(res);
    }

    @Transactional
    public Restaurant addMenu(Long restaurantId, CreateMenuDTO menuDTO) throws InstanceNotFoundException {
        Menu menu = menuService.createMenu(menuDTO);
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurant.addMenu(menu);
        return restaurantRepository.save(restaurant);
    }

    public void verifyAccess(Authentication authentication, Long restaurantId) {
        Restaurant restaurant;
        try {
            restaurant = getRestaurantById(restaurantId);
        } catch (InstanceNotFoundException ex) {
            throw new ApiException(404, "restaurant.not-found");
        }
        UUID userId = KeycloakUtils.getUserUuidFromAuth(authentication);
        if (!restaurant.getAdmin().getUserId().equals(userId)) {
            throw new ApiException(403, "user.no-access");
        }
    }

    private RestaurantSmallDTO toSmallDTO(Restaurant restaurant) {
        return RestaurantSmallDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .build();
    }

    @Autowired
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setRestaurantAdminService(RestaurantAdminService restaurantAdminService) {
        this.restaurantAdminService = restaurantAdminService;
    }
}
