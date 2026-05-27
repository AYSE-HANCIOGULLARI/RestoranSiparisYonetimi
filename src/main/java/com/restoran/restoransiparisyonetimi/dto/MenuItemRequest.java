package com.restoran.restoransiparisyonetimi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MenuItemRequest {

    @NotBlank(message = "Ürün adı boş olamaz")
    private String name;

    private String description;

    @NotNull(message = "Fiyat boş olamaz")
    @Positive(message = "Fiyat pozitif olmalı")
    private Double price;

    @NotBlank(message = "Kategori boş olamaz")
    private String category;


    private boolean available = true;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}