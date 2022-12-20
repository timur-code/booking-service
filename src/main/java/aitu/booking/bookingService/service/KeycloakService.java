package aitu.booking.bookingService.service;

import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak-master.realm}")
    private String masterRealmName;
    @Value("${keycloak-master.username}")
    private String masterUsername;
    @Value("${keycloak-master.password}")
    private String masterPassword;
    @Value("${keycloak-master.client-id}")
    private String masterClientId;

    @Value("${keycloak.realm}")
    private String realmName;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private String GROUP_USERS = "users";

    public Keycloak getMasterKeycloakInstance() {
        log.info("[KEYCLOAK] Server: {}", serverUrl);
        return Keycloak.getInstance(serverUrl, masterRealmName, masterUsername, masterPassword, masterClientId);
    }

    public UserRepresentation getUserById(String id) {
        return getMasterKeycloakInstance()
                .realm(realmName)
                .users()
                .get(id)
                .toRepresentation();
    }

    public UserRepresentation getUserByUsername(String username) {
        List<UserRepresentation> list = getMasterKeycloakInstance()
                .realm(realmName)
                .users()
                .search(username, true);

        return list.size() > 0 ? list.get(0) : null;
    }

    public List<UserRepresentation> search(String searchStr, Integer from, Integer to) {
        return getMasterKeycloakInstance().realm(realmName).users().search(searchStr, from, to - from);
    }
}
