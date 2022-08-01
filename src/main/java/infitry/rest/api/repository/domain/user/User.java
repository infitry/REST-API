package infitry.rest.api.repository.domain.user;

import infitry.rest.api.repository.domain.common.BaseTimeEntity;
import infitry.rest.api.repository.domain.user.code.Authority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter
@Entity(name = "tb_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Enumerated(EnumType.STRING)
    Authority authority;

    @Column(columnDefinition = "varchar(50)")
    String id;

    @Column(columnDefinition = "varchar(50)")
    String name;

    @Column(columnDefinition = "varchar(100)")
    String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.authority.name()));
    }

    @Override
    public String getUsername() {
        return this.id;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}