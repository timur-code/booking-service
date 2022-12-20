package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.auth.UserDTO;
import aitu.booking.bookingService.util.KeycloakUtils;
import lombok.extern.log4j.Log4j2;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserService {
    private KeycloakService keycloakService;


    public UserDTO findUserByPhone(String phone) {
        UserRepresentation user = keycloakService.getUserByUsername(phone);
        return user == null ? null : KeycloakUtils.convertToUserDTO(user);
    }

//    @Cacheable(value = CacheConfig.CACHE_USER)
    public UserDTO findById(String id) {
        return KeycloakUtils.convertToUserDTO(keycloakService.getUserById(id));
    }

    public boolean existsByPhone(String phone) {
        return findUserByPhone(phone) != null;
    }

    public boolean existsByIin(String phone) {
        throw new RuntimeException("Not implemented");
    }

    public List<UserDTO> search(String searchStr, Integer pageNum, Integer pageSize) {
        Integer from = (pageNum - 1) * pageSize;
        Integer to = pageNum * pageSize;
        return keycloakService.search(searchStr, from, to)
                .stream()
                .map(KeycloakUtils::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private String getUserIdByAuth(Authentication authentication) {
        return  ((JwtAuthenticationToken) authentication).getToken().getSubject();
    }

    @Autowired
    public void setKeycloakService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }
}
