package infitry.rest.api.repository;

import infitry.rest.api.repository.domain.order.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @EntityGraph(attributePaths = {"order"})
    Optional<OrderItem> findByOrderItemId(Long orderItemId);
}
