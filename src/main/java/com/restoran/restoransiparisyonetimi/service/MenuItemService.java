package com.restoran.restoransiparisyonetimi.service;

import com.restoran.restoransiparisyonetimi.dto.MenuItemRequest;
import com.restoran.restoransiparisyonetimi.entity.MenuItem;
import com.restoran.restoransiparisyonetimi.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // Tüm menüyü getir
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // ID ile tek ürün getir
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Menü ürünü bulunamadı: " + id));
    }

    // Kategoriye göre filtrele
    public List<MenuItem> getByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    // Sadece müsait olanları getir
    public List<MenuItem> getAvailableItems() {
        return menuItemRepository.findByAvailable(true);
    }

    // Yeni ürün ekle
    public MenuItem createMenuItem(MenuItemRequest request) {
        // Aynı isimde ürün var mı kontrol et
        if (menuItemRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Bu isimde ürün zaten mevcut: " + request.getName());
        }

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        item.setAvailable(true); // Yeni ürün varsayılan olarak müsait

        return menuItemRepository.save(item);
    }

    // Ürün güncelle
    public MenuItem updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem item = getMenuItemById(id); // bulamazsa zaten 404 fırlatır

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());

        return menuItemRepository.save(item);
    }

    // Ürün sil
    public void deleteMenuItem(Long id) {
        MenuItem item = getMenuItemById(id); // bulamazsa 404 fırlatır
        menuItemRepository.delete(item);
    }
}