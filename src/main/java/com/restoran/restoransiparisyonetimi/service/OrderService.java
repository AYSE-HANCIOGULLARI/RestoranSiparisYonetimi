package com.restoran.restoransiparisyonetimi.service;

import com.restoran.restoransiparisyonetimi.dto.OrderRequest;
import com.restoran.restoransiparisyonetimi.entity.MenuItem;
import com.restoran.restoransiparisyonetimi.entity.Order;
import com.restoran.restoransiparisyonetimi.repository.MenuItemRepository;
import com.restoran.restoransiparisyonetimi.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersByCustomer(String customerName) {
        return orderRepository.findByCustomerName(customerName);
    }

    public List<Object[]> getMostOrderedItems() {
        return orderRepository.findMostOrderedItems();
    }

    @Transactional
    public Order createOrder(OrderRequest request) {
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Menü ürünü bulunamadı: " + request.getMenuItemId()));

        if (!menuItem.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bu ürün şu an müsait değil: " + menuItem.getName());
        }

        double totalPrice = menuItem.getPrice() * request.getQuantity();

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");
        order.setOrderTime(LocalDateTime.now());
        order.setMenuItem(menuItem);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long id, String newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sipariş bulunamadı: " + id));

        if ("READY".equals(order.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Teslim edilmiş sipariş değiştirilemez.");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sipariş bulunamadı: " + id));

        if (!"PENDING".equals(order.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Sadece beklemedeki siparişler iptal edilebilir.");
        }

        orderRepository.delete(order);
    }
}