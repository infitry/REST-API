package infitry.rest.api.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String id;
    String password;
    String name;
    String phoneNumber;
    String email;
    AddressDto addressDto;
}
