package infitry.rest.api.util;

import infitry.rest.api.repository.domain.user.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtil {
    public User getLoginUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }

    public Long getLoginUserId() {
        if (getLoginUser() == null) {
            return null;
        }
        return getLoginUser().getUserId();
    }
}
