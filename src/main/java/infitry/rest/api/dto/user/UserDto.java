package infitry.rest.api.dto.user;

import infitry.rest.api.repository.domain.user.Authority;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long userId;
    String id;
    String password;
    String name;
    String phoneNumber;
    String email;
    List<AuthorityDto> authorities;
    AddressDto addressDto;
}
