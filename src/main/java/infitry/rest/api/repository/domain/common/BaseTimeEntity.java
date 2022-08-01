package infitry.rest.api.repository.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @CreatedDate
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;
}
