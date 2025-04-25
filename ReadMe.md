 # 🛠️ Kurulum ve Çalıştırma

Spring bağımlılıkları: 
## 🧩 Kullanılan Teknolojiler ve Bağımlılıklar

| Teknoloji / Bağımlılık | Açıklama                                          | Gerekli        |
|------------------------|---------------------------------------------------|----------------|
| Spring Boot            | Java tabanlı web uygulama çatısı                  | ✅              |
| Docker Compose         | Servisleri konteyner ortamında yönetmek için      | ✅              |
| PostgreSQL             | Veritabanı yönetim sistemi                        | ✅              |
| MinIO                  | S3 uyumlu nesne depolama sistemi                  | ✅              |
| Lombok                 | Getter, Setter gibi kodları otomatik oluşturur   | ✅(çalışmıyor ) |
| Spring Data JPA        | ORM (Hibernate) kullanımı                         | ✅              |
| Maven                  | Proje yapılandırma ve bağımlılık yönetimi        | ✅              |
| core-storage (Repsy)   | Harici çekirdek depolama modülü                   | ✅              |
| file-system-storage    | Dosya sistemi tabanlı depolama                    | ✅              |
| object-storage         | Nesne tabanlı depolama modülü                     | ✅              |


---

 ## 1. Gereksinimler
   Aşağıdaki yazılımlar sisteminizde kurulu olmalıdır:

* Java 17+

* Maven 3.8+

* Docker (MinIO kullanımı için)

* Git (proje klonlamak için)

* Repsy hesabı (paketleri yüklemek/çekmek için)

---

## 2. Projeyi Klonlayın
```sh
git clone https://github.com/sagdicberk/restApi-spring-storage.git
cd restApi
```

---

## 3. KOnfigurasyon updateleri 

Sadece aşağıda bulunun alanları güncellemenizi öneriyorum. kalan kısımları yaml dosyasıyla uyumlu olduğu için dikkatli değiştirin.

````properties
# file-system or object-storage
storage.strategy=object-storage

# Path for file-system
storage.file.base-path=PackageRepo
````

---

## 4. Uygulamayı Derleyin ve Başlatın
````shell
mvn clean install
mvn spring-boot:run
````

---

## 5. Api Guidelines
## 🎯 1. Paket Yükleme (Upload)
Endpoint:

```http request
POST /{packageName}/{version}
Content-Type: multipart/form-data
```
### Açıklama:

Belirtilen packageName ve version bilgisiyle, .json uzantılı metadata dosyası ve .rep uzantılı paket dosyası yüklenir.

#### Form Parametreleri:


| Alan Adı | Açıklama                              | Zorunlu |
|----------|----------------------------------------|---------|
| meta     | `.json` formatında metadata dosyası   | ✅      |
| package  | `.rep` (ZIP) uzantılı paket dosyası   | ✅      |

````bash
curl -X POST http://localhost:8080/my-library/1.0.0 \
  -F "meta=@meta.json" \
  -F "package=@library.rep"
````
Yanıt: 
````http
200 OK
Package uploaded successfully.
````

---

## 📥 2. Paket İndirme (Download)
Endpoint:

````http
GET /{packageName}/{version}/{fileName}
````
### Açıklama:

Belirtilen packageName, version ve fileName bilgilerine göre ilgili paketi indirir.

#### Örnek:

````http
GET /my-library/1.0.0/library.rep
````
Başarılı Yanıt:

````http
200 OK
Content-Disposition: attachment; filename="library.rep"
Content-Type: application/octet-stream
````

---

# ⚙️ Geliştirme Süreci (Development Process)

## 1. Model, DTO ve Repository

* PackageMeta modeli paket bilgilerini (isim, versiyon) tutar.

* MetaDto, ObjectMapper kullanımı için oluşturuldu.

* Temel veri işlemleri için bir JPA repository arayüzü tanımlandı.

## 2. Service Katmanı

* Soyut-somut yapı izlenerek MetaService yazıldı.

* Kayıt öncesi benzersizlik kontrolü yapılır.

* DTO <-> Entity dönüşümleri içerir.

## 3. Storage Katmanı

* Ortak bir interface (StorageService) tanımlandı.

* Dosya sistemi için: proje dizininde depolama.

* Object Storage için: MinIO yapılandırması (MinioConfig) ve bucket yönetimi.

## 4. Strategy Design Pattern
* StorageStrategyContext sınıfı, yapılandırma dosyasına göre uygun stratejiyi belirler.

* Spring @Service anotasyonu ile yönetilir.

## 5. PackageService ve API
* İki ana uç noktası vardır: yükleme ve indirme.

* StrategyContext ile storage seçimi yapılır.

* Postman ile test edilmiştir.

## 6. Modül Ayrıştırması
* Üç bağımsız modül:

    - core-storage: interface ve ortak yapılar 
    - file-system-storage: dosya sistemi implementasyonu 
    - object-storage: MinIO implementasyonu

* pom.xml dosyaları modüler yapıya göre güncellendi.

## 7. Maven Kütüphane Yapısı
* Tüm modüller bağımsız olarak build edilir ve kullanılabilir.

* Ana uygulama bu modülleri bağımlılık olarak kullanır.

## 8. Repsy Yayınlama
* Her modül Repsy Maven Repository’e yayınlandı.

* Test için local repo silindi ve Repsy’den çekme denemesi yapıldı — başarıyla çalıştı.

