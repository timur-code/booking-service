package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.RestaurantAdminDTO;
import aitu.booking.bookingService.model.RestaurantAdmin;
import aitu.booking.bookingService.repository.RestaurantAdminRepository;
import aitu.booking.bookingService.util.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantAdminService {
    private RestaurantAdminRepository repository;
    private UserApi userApi;

    public RestaurantAdmin createAdmin(Long restaurantId, String phone, String password)
            throws JsonProcessingException {
        RestaurantAdminDTO dto = RestaurantAdminDTO.builder()
                .restaurantId(restaurantId)
                .phone(phone)
                .password(password)
                .build();


        RestaurantAdminDTO res = userApi.createAdmin(dto);
        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setUserId(res.getId());
        admin.setPhone(res.getPhone());
        return repository.save(admin);
    }

    @Autowired
    public void setRepository(RestaurantAdminRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setUserApi(UserApi userApi) {
        this.userApi = userApi;
    }
}
