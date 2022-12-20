package aitu.booking.bookingService.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class ResponseSuccessWithData<T> extends ResponseSuccess {
    private T data;

    public ResponseSuccessWithData(T data) {
        super();
        this.data = data;
    }
}
