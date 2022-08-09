package infitry.rest.api.repository.domain.user;

import infitry.rest.api.repository.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity(name = "tb_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @OneToMany
    List<Authority> authorities = new ArrayList<>();

    @Column(columnDefinition = "varchar(50)")
    String id;

    @Column(columnDefinition = "varchar(50)")
    String name;

    @Column(columnDefinition = "varchar(100)")
    String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getRole().name())).collect(Collectors.toList());
    }

    private User(List<Authority> authorities, String id, String name, String password) {
        this.authorities = authorities;
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static User createUser(List<Authority> authorities, String id, String name, String password) {
        return new User(authorities, id, name, password);
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