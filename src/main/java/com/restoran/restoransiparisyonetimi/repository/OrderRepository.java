package com.restoran.restoransiparisyonetimi.repository;

import com.restoran.restoransiparisyonetimi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(String status);

    List<Order> findByCustomerName(String customerName);

    long countByStatus(String status);
}