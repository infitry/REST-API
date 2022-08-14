package infitry.rest.api.repository.domain.user;

import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.repository.domain.common.BaseTimeEntity;
import infitry.rest.api.repository.domain.embedded.Address;
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

    @Column(columnDefinition = "varchar(100)")
    String phoneNumber;

    @Column(columnDefinition = "varchar(100)")
    String email;

    @Embedded
    Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getRole().name())).collect(Collectors.toList());
    }

    private User(List<Authority> authorities, UserDto userDto) {
        this.authorities = authorities;
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.password = userDto.getPassword();
        this.phoneNumber = userDto.getPhoneNumber();
        this.email = userDto.getEmail();
        this.address = Address.createAddress(userDto.getAddressDto());
    }

    public static User createUser(List<Authority> authorities, UserDto userDto) {
        return new User(authorities, userDto);
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