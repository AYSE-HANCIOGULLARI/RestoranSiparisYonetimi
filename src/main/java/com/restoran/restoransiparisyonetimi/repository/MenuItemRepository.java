package com.restoran.restoransiparisyonetimi.repository;

import com.restoran.restoransiparisyonetimi.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategory(String category);

    List<MenuItem> findByAvailable(boolean available);

    List<MenuItem> findByPriceLessThan(Double price);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}