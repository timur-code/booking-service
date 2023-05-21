package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.RestaurantAdminDTO;
import aitu.booking.bookingService.model.RestaurantAdmin;
import aitu.booking.bookingService.repository.RestaurantAdminRepository;
import aitu.booking.bookingService.util.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantAdminService {
    private RestaurantAdminRepository repository;
    private UserApi userApi;

    @SneakyThrows
    public RestaurantAdmin createAdmin(Long restaurantId, String phone, String password) {
        RestaurantAdminDTO dto = RestaurantAdminDTO.builder()
                .restaurantId(restaurantId)
                .phone(phone)
                .password(password)
                .build();


        RestaurantAdminDTO res = userApi.createAdmin(dto);
        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setPhone(phone);
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
