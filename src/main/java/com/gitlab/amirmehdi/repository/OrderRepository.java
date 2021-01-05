package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByBourseCode_BrokerAndOmsId(Broker broker, String omsId);

    List<Order> findAllByStateIn(List<OrderState> states);

    List<Order> findAllByState(OrderState state);

    List<Order> findAllByExecutedGreaterThanAndCreatedAtGreaterThan(int executed, Date createdAt);
}
