🌐 RProxy | Enterprise Country-Based Access Control & Anti-Proxy Defense
🇬🇧 English
Overview
RProxy is a lightweight, high-performance Country-Based Access Control and Anti-Proxy Defense plugin designed specifically for Spigot servers. It acts as a security gate against regional bot attacks and unauthorized VPN/proxy entries by inspecting incoming connection attempts during the AsyncPlayerPreLoginEvent phase and comparing IP geolocations against strict security whitelists—all with zero external dependencies and maximum performance.

✨ Features
🛡️ Async Network Filtering: Queries IP geolocation without blocking the main server thread or dropping TPS.

🌐 ISO Country Whitelisting: Easily grant or restrict access using standard two-letter ISO 3166-1 alpha-2 country codes (e.g., TR, US, GB).

🔑 Player & UUID Bypass: Exempt trusted staff members or specific players from country restrictions.

⚡ Zero Dependencies: Built with native Java networking and regex JSON parsing for ultra-fast execution and low memory usage.

📢 Real-Time Staff Telemetry: Sends instant broadcast alerts to online staff when an unauthorized login attempt is blocked.

⚙️ Fully Configurable & Localized: Multi-language support out of the box via external language templates (lang/tr.yml and lang/eng.yml).

🚀 Installation
Download the latest .jar file from the Releases section.

Place it into your server's plugins folder.

Restart or start your server.

Customize settings in plugins/RProxy/config.yml and language files in plugins/RProxy/lang/.

🇹🇷 Türkçe
Genel Bakış
RProxy, Spigot sunucuları için özel olarak geliştirilmiş hafif, yüksek performanslı Ülke Tabanlı Erişim Kontrolü ve Anti-Proxy koruma eklentisidir. AsyncPlayerPreLoginEvent aşamasında gelen bağlantı denetimlerini gerçekleştirip IP adreslerinin coğrafi konumlarını sıkı güvenlik beyaz listeleriyle karşılaştırarak, sunucu performansını (TPS) düşürmeden bölgesel bot saldırılarını ve yetkisiz VPN/proxy girişlerini engeller.

✨ Özellikler
🛡️ Asenkron Ağ Filtreleme: Ana sunucu akışını kitlemeden ve TPS düşürmeden IP doğrulamasını gerçekleştirir.

🌐 ISO Ülke Beyaz Listesi: Standart iki haneli ISO 3166-1 alpha-2 ülke kodları (ör. TR, US, GB) ile sunucu giriş izinlerini kolayca yönetin.

🔑 Oyuncu & UUID Atlatma: Yetkilileri veya güvenilir oyuncuları isim ya da UUID bazlı kontrol dışı bırakın.

⚡ Sıfır Bağımlılık: Ağır harici kütüphaneler içermez; yerel Java ağ altyapısı ve regex JSON ayrıştırma ile maksimum hız sunar.

📢 Gerçek Zamanlı Yetkili Bildirimi: Yetkisiz bir giriş denemesi engellendiğinde çevrimiçi yetkililere anlık sohbet bildirimi gönderir.

⚙️ Tamamen Özelleştirilebilir: lang/tr.yml ve lang/eng.yml harici dil dosyaları üzerinden tüm mesajları dilediğiniz gibi düzenleyin.

🚀 Kurulum
En son .jar dosyasını Releases sayfasından indirin.

Dosyayı sunucunuzun plugins klasörüne atın.

Sunucunuzu başlatın veya yeniden başlatın.

Ayarlarınızı plugins/RProxy/config.yml ve dil dosyalarınızı plugins/RProxy/lang/ klasöründen yapılandırın.

👨‍💻 Developer & License
Developer: ReatyzR

Version: 1.0.0

License: MIT License
