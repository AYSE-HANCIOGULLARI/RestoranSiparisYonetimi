package com.restoran.restoransiparisyonetimi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {

    @NotBlank(message = "Müşteri adı boş olamaz")
    private String customerName;

    @NotNull(message = "Menü ürünü seçilmeli")
    private Long menuItemId;

    @NotNull(message = "Miktar boş olamaz")
    @Positive(message = "Miktar pozitif olmalı")
    private Integer quantity;

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}