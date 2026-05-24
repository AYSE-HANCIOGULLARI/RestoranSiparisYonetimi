package com.restoran.restoransiparisyonetimi.controller;

import com.restoran.restoransiparisyonetimi.dto.OrderRequest;
import com.restoran.restoransiparisyonetimi.entity.Order;
import com.restoran.restoransiparisyonetimi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET /api/orders → Tüm siparişler
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /api/orders/status/PENDING → Duruma göre filtrele
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    // GET /api/orders/customer/Ahmet → Müşteriye göre filtrele
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<Order>> getByCustomer(@PathVariable String customerName) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerName));
    }

    // POST /api/orders → Yeni sipariş oluştur
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest request) {
        Order created = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PATCH /api/orders/5/status?newStatus=PREPARING → Durum güncelle
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    // DELETE /api/orders/5 → Sipariş iptal (sadece PENDING)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}