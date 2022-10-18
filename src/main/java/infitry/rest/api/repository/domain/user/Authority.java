package infitry.rest.api.repository.domain.user;

import infitry.rest.api.dto.user.AuthorityDto;
import infitry.rest.api.repository.domain.common.BaseTimeEntity;
import infitry.rest.api.repository.domain.user.code.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Entity(name = "tb_authority")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long authorityId;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column
    String description;

    private Authority(Role role, String description) {
        this.role = role;
        this.description = description;
    }

    public static Authority createAuthority(Role role, String description) {
        return new Authority(role, description);
    }

    /** Entity to Dto */
    public AuthorityDto toDto() {
        return AuthorityDto.builder()
                    .authorityId(this.authorityId)
                    .role(this.role)
                    .description(this.description)
                .build();
    }
}