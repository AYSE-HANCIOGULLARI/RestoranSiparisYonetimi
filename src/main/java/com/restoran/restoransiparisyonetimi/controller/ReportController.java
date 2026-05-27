package com.restoran.restoransiparisyonetimi.controller;

import com.restoran.restoransiparisyonetimi.repository.MenuItemRepository;
import com.restoran.restoransiparisyonetimi.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final OrderService orderService;
    private final MenuItemRepository menuItemRepository;

    public ReportController(OrderService orderService, MenuItemRepository menuItemRepository) {
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/most-ordered")
    public ResponseEntity<List<Object[]>> getMostOrdered() {
        return ResponseEntity.ok(orderService.getMostOrderedItems());
    }

    @GetMapping("/expensive-items")
    public ResponseEntity<?> getExpensiveItems() {
        return ResponseEntity.ok(menuItemRepository.findByOrderByPriceDesc());
    }

    @GetMapping("/cheap-items")
    public ResponseEntity<?> getCheapItems() {
        return ResponseEntity.ok(menuItemRepository.findByOrderByPriceAsc());
    }
}