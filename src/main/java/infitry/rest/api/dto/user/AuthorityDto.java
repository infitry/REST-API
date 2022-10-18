package infitry.rest.api.dto.user;

import infitry.rest.api.repository.domain.user.code.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorityDto {
    Long authorityId;
    Role role;
    String description;
}
