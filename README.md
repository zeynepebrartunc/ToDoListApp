
# To-Do List App (Takvim Entegreli) 📝

Bu proje, kullanıcıların günlük görevlerini yönetmelerine olanak tanıyan, takvim destekli bir Android uygulamasıdır. Her gün için ayrı bir yapılacaklar listesi tutulmasını sağlar ve verileri cihazda kalıcı olarak saklar.

## 🚀 Özellikler

* **Takvim Entegresi:** `CalendarView` kullanarak farklı günler arasında geçiş yapabilme.
* **Görevi Kaydetme:** Görevler, seçilen tarihe özel olarak kaydedilir.
* **Veri Kalıcılığı:** Uygulama kapansa bile görevler `SharedPreferences` ve `JSON` formatı kullanılarak cihazda saklanır.
* **CRUD İşlemleri:**
* **Create:** Yeni görev ekleme.
* **Read:** Seçilen tarihe ait görevleri listeleme.
* **Update:** Görev durumunu (yapıldı/yapılmadı) güncelleme.
* **Delete:** Tekli silme (Adapter üzerinden) veya tüm listeyi temizleme.


* **Dinamik Arayüz:** `RecyclerView` ve `Custom Adapter` kullanımı ile akıcı bir kullanıcı deneyimi.

---

## 🛠 Kullanılan Teknolojiler ve Yapılar

* **Dil:** Java
* **UI Bileşenleri:** `RecyclerView`, `CalendarView`, `AlertDialog`, `CardView`.
* **Veri Yönetimi:** * `SharedPreferences`: Küçük boyutlu verileri anahtar-değer çifti olarak saklamak için.
* `JSONArray` & `JSONObject`: Görev listesini metin formatına dönüştürüp saklamak için.


* **Mimari:** Temel Android Activity yapısı ve Event-driven (Olay güdümlü) programlama.

---

## 📂 Kod Yapısı

### `MainActivity.java`

Uygulamanın ana motorudur. Şu temel fonksiyonları içerir:

* `loadTasks()`: Seçili tarihteki görevleri JSON formatından Java nesnelerine çevirir.
* `saveTasks()`: Mevcut listeyi JSON'a dönüştürüp tarihe göre diske yazar.
* `buildDialog()`: Kullanıcıdan yeni görev almak için bir pencere açar.

### `TaskAdapter.java` (Tahmini)

Görevlerin listede nasıl görüneceğini kontrol eder ve silme/tamamlama gibi etkileşimleri yönetir.

### `Task.java` (Model)

Görev ismini ve tamamlanma durumunu tutan veri modelidir.

---

## 📝 Notlar

* Uygulama, verileri `yyyy-MM-dd` formatında anahtarlar kullanarak saklar.
* `SharedPreferences` kullanıldığı için uygulama verileri temizlenmediği sürece görevleriniz güvende kalır.
