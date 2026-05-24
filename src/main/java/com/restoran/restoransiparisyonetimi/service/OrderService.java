package com.restoran.restoransiparisyonetimi.service;

import com.restoran.restoransiparisyonetimi.dto.OrderRequest;
import com.restoran.restoransiparisyonetimi.entity.MenuItem;
import com.restoran.restoransiparisyonetimi.entity.Order;
import com.restoran.restoransiparisyonetimi.repository.MenuItemRepository;
import com.restoran.restoransiparisyonetimi.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // Tüm siparişleri getir
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Duruma göre filtrele (PENDING, PREPARING, READY)
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    // Müşteriye göre filtrele
    public List<Order> getOrdersByCustomer(String customerName) {
        return orderRepository.findByCustomerName(customerName);
    }

    // Yeni sipariş oluştur — iş mantığı burada
    public Order createOrder(OrderRequest request) {
        // 1. Ürün veritabanında var mı?
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Menü ürünü bulunamadı: " + request.getMenuItemId()));

        // 2. Ürün müsait mi?
        if (!menuItem.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bu ürün şu an müsait değil: " + menuItem.getName());
        }

        // 3. Toplam fiyatı hesapla
        double totalPrice = menuItem.getPrice() * request.getQuantity();

        // 4. Siparişi oluştur
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");
        order.setOrderTime(LocalDateTime.now());
        order.setMenuItem(menuItem);

        return orderRepository.save(order);
    }

    // Sipariş durumunu güncelle (PENDING → PREPARING → READY)
    public Order updateOrderStatus(Long id, String newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sipariş bulunamadı: " + id));

        // İş kuralı: READY siparişi tekrar değiştirilemez
        if ("READY".equals(order.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Teslim edilmiş sipariş değiştirilemez.");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // Sipariş iptal et — sadece PENDING iken mümkün
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