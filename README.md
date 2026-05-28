# 🍽️ Restoran Sipariş Yönetim Sistemi

Bu proje, modern bir restoranın menü yönetimini, anlık sipariş takibini ve mutfak operasyonlarını dijitalleştirmek amacıyla geliştirilmiş **Spring Boot** tabanlı bir web uygulamasıdır. Gelişmiş backend mimarisi ve kullanıcı dostu arayüzü sayesinde restoran içi süreçlerin uçtan uca, hatasız ve hızlı bir şekilde yönetilmesini sağlar.

---

## Özellikler

- **Dinamik Menü Yönetimi:** Yeni yemekler eklenebilir, güncellenebilir ve listelenebilir.
- **Anlık Sipariş Girişi:** Masalardan gelen siparişler sisteme anında işlenir.
- **Sipariş Durum Takibi:** `PENDING` → `PREPARING` → `READY` akışı ile mutfak yönetimi.
- **PUT & PATCH Desteği:** RESTful API standartlarına uygun güncelleme.
- **Gelişmiş Raporlama:** En çok sipariş edilen ürünler, fiyat sıralamaları.
- **RBAC Güvenlik:** Her rol sadece kendi yetkisindeki endpointlere erişebilir.
- **Derived Query Methods:** JPA metot adından otomatik SQL üretimi.
- **Global Exception Handler:** Hatalarda anlamlı JSON mesajları döner.

---

## 🛠️ Kullanılan Teknolojiler

| Teknoloji | Açıklama |
|---|---|
| Java 17 | Programlama dili |
| Spring Boot 3.2.5 | Ana framework |
| Spring Data JPA | Repository ve Derived Query |
| Spring Security | Basic Auth + RBAC |
| H2 Database | Bellek içi veritabanı |
| Thymeleaf | Server-side template engine |
| Tailwind CSS | Frontend tasarım |
| Jakarta Validation | DTO doğrulama (@NotNull, @Size) |
| Maven | Bağımlılık yönetimi |

---

## 📁 Proje Mimarisi
HTTP İsteği (Postman / Tarayıcı)
↓
Controller        ← HTTP isteklerini karşılar
↓
Service          ← İş mantığı ve kurallar
↓
Repository        ← CRUD + Derived Query Methods
↓
H2 Database        ← Bellek içi veritabanı
### Katman Yapısı
src/main/java/com/restoran/restoransiparisyonetimi/
├── config/          → SecurityConfig (RBAC tanımları)
├── controller/      → MenuItemController, OrderController, ReportController,ViewController
├── dto/             → LoginRequest, MenuItemRequest, OrderRequest
├── entity/          → User, MenuItem, Order
├── exception/       → GlobalExceptionHandler
├── repository/      → UserRepository, MenuItemRepository, OrderRepository
└── service/         → MenuItemService, OrderService
---


## 🔍 Derived Query Methods

JPA'nın metot adından otomatik SQL üretme özelliği kullanılmıştır:

```java
// MenuItemRepository
List<MenuItem> findByCategory(String category);
List<MenuItem> findByAvailable(boolean available);
List<MenuItem> findByPriceLessThan(Double price);
List<MenuItem> findByNameContainingIgnoreCase(String name);
boolean existsByName(String name);
List<MenuItem> findByOrderByPriceDesc();
List<MenuItem> findByOrderByPriceAsc();

// OrderRepository
List<Order> findByStatus(String status);
List<Order> findByCustomerName(String customerName);
long countByStatus(String status);
```

---

## 💻 Kurulum ve Çalıştırma

### Gereksinimler
- Java 17+
- Maven
- IntelliJ IDEA

### Adımlar

**1. Projeyi klonla:**
```bash
git clone https://github.com/KULLANICI_ADIN/RestoranSiparisYonetimi.git
cd RestoranSiparisYonetimi
```

**2. Projeyi IntelliJ'de aç:**
- File → Open → klasörü seç
- Maven bağımlılıklarının yüklenmesini bekle

**3. Uygulamayı başlat:**
- `RestoranSiparisYonetimiApplication.java` dosyasını aç
- Yeşil Run butonuna bas

**4. Tarayıcıda aç:**
http://localhost:8080

**5. H2 Veritabanı Konsolu:**
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:restorandb
Kullanıcı: sa
Şifre:    (boş bırak)
---
## 📡 API Endpoints & Rol Bazlı Yetkilendirme

Projedeki tüm API endpoint'leri Spring Security katmanında Basic Authentication ile korunmakta, kullanıcı rollerine ve yetkilerine göre dinamik olarak sınırlandırılmaktadır.

### 🍔 Menü Endpoints

| Metot | URL | Yetki (Role) | Açıklama |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/menu-items` | Herkese Açık (`permitAll`) | Tüm menüyü listele |
| **GET** | `/api/menu-items/{id}` | Herkese Açık (`permitAll`) | Tek bir ürünün detayını getir |
| **GET** | `/api/menu-items/category/{category}` | Herkese Açık (`permitAll`) | Ürünleri kategorisine göre listele |
| **GET** | `/api/menu-items/available` | Herkese Açık (`permitAll`) | Stokta/Müsait olan ürünleri listele |
| **POST** | `/api/menu-items` | `ADMIN` | Menüye yeni yemek/ürün ekle |
| **PUT** | `/api/menu-items/{id}` | `ADMIN`, `WAITER` | Menüdeki mevcut ürünü güncelle |
| **DELETE** | `/api/menu-items/{id}` | `ADMIN` | Menüden ürün sil |

### 📝 Sipariş Endpoints

| Metot | URL | Yetki (Role) | Açıklama |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/orders` | Giriş Yapan Herkes (`authenticated`) | Tüm siparişleri listele |
| **GET** | `/api/orders/status/{status}` | Giriş Yapan Herkes (`authenticated`) | Siparişleri durumuna göre filtrele |
| **GET** | `/api/orders/customer/{name}` | Giriş Yapan Herkes (`authenticated`) | Siparişleri müşteri adına göre filtrele |
| **POST** | `/api/orders` | `ADMIN`, `WAITER`, `CUSTOMER` | Yeni bir sipariş oluştur |
| **PATCH** | `/api/orders/{id}/status` | `ADMIN`, `WAITER` | Sipariş durumunu kısmi güncelle (`PREPARING`) |
| **PUT** | `/api/orders/{id}` | Giriş Yapan Herkes (`authenticated`) | Sipariş detaylarını tamamen güncelle |
| **DELETE** | `/api/orders/{id}` | Giriş Yapan Herkes (`authenticated`) | Siparişi iptal et veya sistemden sil |

### 📊 Rapor Endpoints

| Metot | URL | Yetki (Role) | Açıklama |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/reports/most-ordered` | Giriş Yapan Herkes (`authenticated`) | En çok sipariş edilen ürünlerin istatistiğini getir |
| **GET** | `/api/reports/expensive-items` | Giriş Yapan Herkes (`authenticated`) | Menüdeki ürünleri pahalıdan ucuza sırala |
| **GET** | `/api/reports/cheap-items` | Giriş Yapan Herkes (`authenticated`) | Menüdeki ürünleri ucuzdan pahalıya sırala |

---

## 👥 Sistem Kullanıcıları ve Test Hesapları

Uygulamayı farklı rollerle ve yetki sınırlarıyla test edebilmek amacıyla `SecurityConfig` içerisinde in-memory olarak tanımlanmış kurumsal hesap bilgileri aşağıdadır:

* 🔐 **Yönetici (Admin):** `admin` / `1214` (Sistem genelinde tam yetki)
* 🔐 **Garson (Waiter):** `garson` / `1214` (Sipariş oluşturma, mutfak durumlarını güncelleme ve menü düzenleme yetkisi)
* 🔐 **Müşteri (Customer):** `musteri` / `1214` (Sadece menü kataloglarını görme ve yeni sipariş geçme yetkisi)


## 📬 Postman ile Test

### 1. Menüye Ürün Ekle (ADMIN)
Metot:  POST
URL:    http://localhost:8080/api/menu-items
Auth:   Basic Auth → admin / 1214
Body (raw JSON):
{
"name": "Margherita Pizza",
"description": "Domates soslu, mozzarella peynirli",
"price": 180.0,
"category": "Pizza"
}
Beklenen cevap: 201 Created
### 2. Menüyü Listele (Herkese Açık)
Metot:  GET
URL:    http://localhost:8080/api/menu-items
Auth:   Basic Auth → admin / 1214
Beklenen cevap: 200 OK + ürün listesi
### 3. Sipariş Oluştur (ADMIN,CUSTOMER,WAITER)
Metot:  POST
URL:    http://localhost:8080/api/orders
Auth:   Basic Auth → musteri / 1214
Body (raw JSON):
{
"customerName": "Ahmet",
"menuItemId": 1,
"quantity": 2
}
Beklenen cevap: 201 Created
### 4. Sipariş Durumunu Güncelle ("WAITER", "ADMIN")
Metot:  PATCH
URL:    http://localhost:8080/api/orders/1/status?newStatus=PREPARING
Auth:   Basic Auth → garson / 1214
Beklenen cevap: 200 OK
### 5. Siparişi İptal Et (ADMIN)
Metot:  DELETE
URL:    http://localhost:8080/api/orders/1
Auth:   Basic Auth → admin / 1214
Beklenen cevap: 204 No Content
### 6. Yetkisiz Erişim Testi
Metot:  DELETE
URL:    http://localhost:8080/api/menu-items/1
Auth:   Basic Auth → musteri / 1214
Beklenen cevap: 403 Forbidden ← RBAC çalışıyor!
---

## 🌐 Frontend Sayfaları

| Sayfa | URL | Açıklama |
|---|---|---|
| Menü Yönetimi | /menu | Ürün ekle, güncelle, sil |
| Siparişler | /orders | Sipariş oluştur ve yönet |
| Raporlar | /reports | Satış ve performans raporları |

---

## ⚠️ Önemli Not

H2 veritabanı **bellek içi** çalıştığı için uygulama her yeniden başlatıldığında veriler sıfırlanır.
