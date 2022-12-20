package aitu.booking.bookingService.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class UserDTO {

    private String id;
    private String name; // fullname
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phone;
    private String email;
    private String iin;
    private Boolean iinVerified;
    private Boolean passwordResetRequired;
    private Boolean hasPrivacyPolicyAgreement;
    private Boolean hasSocialProfile;
    private ZonedDateTime createdAt;

}
