package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByBrokerAndOmsId(Broker broker,String omsId);
}
