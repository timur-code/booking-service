package aitu.booking.bookingService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@AllArgsConstructor
public class FormValidationException extends RuntimeException {

    private BindingResult bindingResult;

}
