package infitry.rest.api.util;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@UtilityClass
public class DateUtil {

    public Date addHours(Long hours) {
        LocalDateTime dateTime = LocalDateTime.now().plusHours(hours);
        return Timestamp.valueOf(dateTime);
    }
    public Date minusHours(Long hours) {
        LocalDateTime dateTime = LocalDateTime.now().minusHours(hours);
        return Timestamp.valueOf(dateTime);
    }
}
