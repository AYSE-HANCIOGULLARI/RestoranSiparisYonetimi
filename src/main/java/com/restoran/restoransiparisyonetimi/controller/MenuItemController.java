package com.restoran.restoransiparisyonetimi.controller;

import com.restoran.restoransiparisyonetimi.dto.MenuItemRequest;
import com.restoran.restoransiparisyonetimi.entity.MenuItem;
import com.restoran.restoransiparisyonetimi.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // GET /api/menu-items → Tüm menüyü getir
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    // GET /api/menu-items/5 → ID ile tek ürün getir
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    // GET /api/menu-items/category/Pizza → Kategoriye göre filtrele
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(menuItemService.getByCategory(category));
    }

    // GET /api/menu-items/available → Müsait ürünler
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableItems() {
        return ResponseEntity.ok(menuItemService.getAvailableItems());
    }

    // POST /api/menu-items → Yeni ürün ekle (ADMIN yetkisi gerekir)
    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItemRequest request) {
        MenuItem created = menuItemService.createMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/menu-items/5 → Ürün güncelle
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, request));
    }

    // DELETE /api/menu-items/5 → Ürün sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}