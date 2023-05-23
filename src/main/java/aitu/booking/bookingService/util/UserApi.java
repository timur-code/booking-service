package aitu.booking.bookingService.util;

import aitu.booking.bookingService.dto.RestaurantAdminDTO;
import aitu.booking.bookingService.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class UserApi {
    @Value("${service.user.token}")
    private String serviceToken;
    @Value("${service.user.url}")
    private String serviceUrl;
    private static volatile UserApi instance;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    private UserApi() {
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request requestWithHeaders = originalRequest.newBuilder()
                            .header("service_token", "token")
                            .build();

                    return chain.proceed(requestWithHeaders);
                })
                .build();
        mapper = new ObjectMapper();
    }

    public static UserApi getInstance() {
        if (instance == null) {
            synchronized (UserApi.class) {
                if (instance == null) {
                    instance = new UserApi();
                }
            }
        }
        return instance;
    }

    public RestaurantAdminDTO createAdmin(RestaurantAdminDTO dto) throws JsonProcessingException {
        RequestBody body = createRequestBody(dto);
        Request request = new Request.Builder()
                .url(serviceUrl + "/create/restaurant-admin")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                int code = response.code();
                log.error("Error creating restaurant admin: {}\n{}", code, response);
                throw new ApiException(code, "admin.create.error");
            }
            String json = response.body().string();
            return mapper.readValue(json, RestaurantAdminDTO.class);
        } catch (IOException e) {
            log.error("Error creating restaurant admin:\n", e);
            throw new ApiException(500, "admin.create.error");
        }
    }

    private RequestBody createRequestBody(Object object) throws JsonProcessingException {
        MediaType mediaType = MediaType.parse("application/json");
        return RequestBody.create(mediaType, mapper.writeValueAsString(object));
    }
}
