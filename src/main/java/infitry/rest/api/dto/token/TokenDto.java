package infitry.rest.api.dto.token;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenDto {
    String accessToken;
    String refreshToken;
}
