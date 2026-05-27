package com.restoran.restoransiparisyonetimi.service;

import com.restoran.restoransiparisyonetimi.dto.MenuItemRequest;
import com.restoran.restoransiparisyonetimi.entity.MenuItem;
import com.restoran.restoransiparisyonetimi.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Menü ürünü bulunamadı: " + id));
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getAvailableItems() {
        return menuItemRepository.findByAvailable(true);
    }

    public MenuItem createMenuItem(MenuItemRequest request) {
        if (menuItemRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Bu isimde ürün zaten mevcut: " + request.getName());
        }

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        item.setAvailable(true);

        return menuItemRepository.save(item);
    }

    public MenuItem updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem item = getMenuItemById(id);
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        return menuItemRepository.save(item);
    }

    public void deleteMenuItem(Long id) {
        MenuItem item = getMenuItemById(id);
        menuItemRepository.delete(item);
    }
}