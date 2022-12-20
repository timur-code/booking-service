package aitu.booking.bookingService.util;

import aitu.booking.bookingService.dto.auth.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class KeycloakUtils {
    public static UserDTO convertToUserDTO(UserRepresentation userRepresentation) {
        UserDTO userDto = new UserDTO();
        userDto.setId(userRepresentation.getId());
        userDto.setPhone(userRepresentation.getUsername());
        userDto.setName(getFullName(userRepresentation));
        userDto.setFirstName(userRepresentation.getFirstName());
        userDto.setLastName(userRepresentation.getLastName());
        userDto.setPatronymic(userRepresentation.firstAttribute("patronymic"));
        userDto.setEmail(userRepresentation.getEmail());
        userDto.setIin(userRepresentation.firstAttribute("iin"));
        userDto.setIinVerified("true".equals(userRepresentation.firstAttribute("iinVerified")));
        userDto.setPasswordResetRequired("true".equals(userRepresentation.firstAttribute("passwordResetRequired")));

        Instant instant = Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp());
        userDto.setCreatedAt(ZonedDateTime.ofInstant(instant, ZoneOffset.UTC));

        return userDto;
    }

    public static UserRepresentation convertToUserRepresentation(UserDTO userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userDTO.getId());
        userRepresentation.setUsername(userDTO.getPhone());
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.singleAttribute("patronymic", StringUtils.hasLength(userDTO.getPatronymic()) ? userDTO.getPatronymic() : null);
        userRepresentation.singleAttribute("phone", userDTO.getPhone());
        userRepresentation.singleAttribute("phoneVerified", "true");
        userRepresentation.singleAttribute("iin", StringUtils.hasLength(userDTO.getIin()) ? userDTO.getIin() : null);
        userRepresentation.singleAttribute("iinVerified", "" + Boolean.TRUE.equals(userDTO.getIinVerified()));
        userRepresentation.singleAttribute("passwordResetRequired", "" + Boolean.TRUE.equals(userDTO.getPasswordResetRequired()));

        return userRepresentation;
    }

    private static String getFullName(UserRepresentation userRepresentation) {
        String firstName = userRepresentation.getFirstName();
        String lastName = userRepresentation.getLastName();
        String patronymic = userRepresentation.firstAttribute("patronymic");

        String fullname = (StringUtils.hasLength(lastName) ? lastName : "") + " " +
                (StringUtils.hasLength(firstName) ? firstName : "") + " " +
                (StringUtils.hasLength(patronymic) ? patronymic : "");

        return fullname.trim();
    }

}
