package aitu.booking.bookingService;

import aitu.booking.bookingService.dto.create.CreateMenuDTO;
import aitu.booking.bookingService.dto.create.CreateMenuItemDTO;
import aitu.booking.bookingService.model.MenuItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class BaseTest {
    private ObjectMapper mapper;

    @SneakyThrows
    public Method getPrivateMethod(Object object, String methodName, Class<?>... parameterClasses) {
        Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
        method.setAccessible(true);
        return method;
    }

    public String toJson(Object object) throws JsonProcessingException {
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(object);
    }

    public CreateMenuItemDTO getCreateMenuItemDTO() {
        return CreateMenuItemDTO.builder()
                .name("Паста")
                .description("Супер крутая паста")
                .images(List.of("https://cdn.lifehacker.ru/wp-content/uploads/2018/04/Pasta_1523360590.jpg"))
                .build();
    }

    public CreateMenuDTO getCreateMenuDTO() {
        return CreateMenuDTO.builder()
                .name("Меню")
                .description("Наше меню")
                .language("ru")
                .build();
    }
}
