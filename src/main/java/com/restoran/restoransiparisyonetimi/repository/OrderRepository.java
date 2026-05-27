package com.restoran.restoransiparisyonetimi.repository;

import com.restoran.restoransiparisyonetimi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(String status);

    List<Order> findByCustomerName(String customerName);
    @Query("SELECT o.menuItem.name, COUNT(o) as orderCount FROM Order o GROUP BY o.menuItem.name ORDER BY orderCount DESC")
    List<Object[]> findMostOrderedItems();

    long countByStatus(String status);
}