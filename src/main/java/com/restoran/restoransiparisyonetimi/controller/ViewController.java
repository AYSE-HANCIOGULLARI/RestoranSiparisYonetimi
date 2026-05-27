package com.restoran.restoransiparisyonetimi.controller;

import com.restoran.restoransiparisyonetimi.entity.Order;
import com.restoran.restoransiparisyonetimi.repository.MenuItemRepository;
import com.restoran.restoransiparisyonetimi.repository.OrderRepository;
import com.restoran.restoransiparisyonetimi.service.MenuItemService;
import com.restoran.restoransiparisyonetimi.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    private final MenuItemService menuItemService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public ViewController(MenuItemService menuItemService,
                          OrderService orderService,
                          OrderRepository orderRepository,
                          MenuItemRepository menuItemRepository) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // Ana sayfa ("/") doğrudan orders.html şablonuna yönleniyor
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "orders";
    }

    // Menü Yönetimi ekranı
    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "menu";
    }

    // Siparişler ekranı da artık index.html yerine DOĞRUDAN orders.html şablonuna yönleniyor!
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "orders";
    }

    // Raporlar ekranı
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("mostOrdered", orderService.getMostOrderedItems());
        model.addAttribute("expensiveItems", menuItemRepository.findByOrderByPriceDesc());
        model.addAttribute("cheapItems", menuItemRepository.findByOrderByPriceAsc());
        return "reports";
    }

    // Giriş ekranı
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}