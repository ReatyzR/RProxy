package dev.reatyzr.rproxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

public final class RProxy extends JavaPlugin implements Listener {
   private static final String DEFAULT_API = "http://ip-api.com/json/{ip}?fields=status,country,countryCode";
   private static final int DEFAULT_TIMEOUT_MS = 3500;
   private static final Pattern COUNTRY_CODE = Pattern.compile("\\\"countryCode\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"", 2);
   private FileConfiguration lang;

   public void onEnable() {
      this.saveDefaultConfig();
      this.ensureLanguageFiles();
      this.loadLanguage();
      this.getServer().getPluginManager().registerEvents(this, this);
      this.getLogger().info("RProxy enabled. Configurable country access gate is active.");
   }

   @EventHandler
   public void onLogin(AsyncPlayerPreLoginEvent var1) {
      String var2 = var1.getName();
      UUID var3 = var1.getUniqueId();
      String var4 = var1.getAddress() == null ? null : var1.getAddress().getHostAddress();
      if (!this.isBypassed(var2, var3)) {
         String var5 = this.lookupCountryCode(var4);
         if (var5 == null) {
            String var7 = this.message("messages.lookup-failed", "&cRProxy &8» &fÜlke doğrulaması yapılamadı.\n&7VPN/Proxy bağlantınızı kapatıp tekrar deneyin.").replace("%player%", var2).replace("%country%", "UNKNOWN").replace("%countries%", this.allowedCountriesText()).replace("%ip%", this.safeAddress(var4));
            var1.disallow(Result.KICK_OTHER, this.color(var7));
            this.notifyStaff("lookup-failed", var2, "UNKNOWN", var4);
         } else {
            if (!this.allowedCountries().contains(var5)) {
               String var6 = this.message("messages.denied-country", "&cRProxy &8» &fBu sunucuya %country% ülkesinden giriş izni yok.\n&7İzin verilen ülkeler: &e%countries%\n&7VPN/Proxy kullanıyorsanız kapatıp tekrar deneyin.").replace("%player%", var2).replace("%country%", var5).replace("%countries%", this.allowedCountriesText()).replace("%ip%", this.safeAddress(var4));
               var1.disallow(Result.KICK_OTHER, this.color(var6));
               this.notifyStaff("denied-country", var2, var5, var4);
            }

         }
      }
   }

   private boolean isBypassed(String var1, UUID var2) {
      List var3 = this.getConfig().getStringList("bypass.players");
      String var4 = var2 == null ? "" : var2.toString();

      for(String var6 : var3) {
         if (var6 != null) {
            String var7 = var6.trim();
            if (var7.equalsIgnoreCase(var1) || var7.equalsIgnoreCase(var4)) {
               return true;
            }
         }
      }

      return false;
   }

   private Set<String> allowedCountries() {
      List var1 = this.getConfig().getStringList("allowed-countries");
      HashSet var2 = new HashSet();

      for(String var4 : var1) {
         if (var4 != null && !var4.trim().isEmpty()) {
            var2.add(var4.trim().toUpperCase(Locale.ROOT));
         }
      }

      if (var2.isEmpty()) {
         var2.addAll(Arrays.asList("TR", "GB", "US"));
      }

      return Collections.unmodifiableSet(var2);
   }

   private String allowedCountriesText() {
      return String.join(", ", this.allowedCountries());
   }

   private void notifyStaff(String var1, String var2, String var3, String var4) {
      if (this.getConfig().getBoolean("settings.broadcast-denied-attempts", false)) {
         final String var5 = this.color(this.message("messages.staff-alert", "&cRProxy &8» &f%player% &7giriş reddedildi. &8(&e%reason%&8) &7Ülke: &e%country% &7IP: &e%ip%").replace("%player%", var2).replace("%reason%", var1).replace("%country%", var3).replace("%ip%", this.safeAddress(var4)));
         final String var6 = this.getConfig().getString("settings.staff-permission", "rproxy.alerts");
         Bukkit.getScheduler().runTask(this, new Runnable() {
            public void run() {
               for(Player var2 : Bukkit.getOnlinePlayers()) {
                  if (var2.hasPermission(var6)) {
                     var2.sendMessage(var5);
                  }
               }

            }
         });
      }
   }

   private String lookupCountryCode(String var1) {
      if (var1 != null && var1.length() != 0 && var1.length() <= 64) {
         HttpURLConnection var2 = null;

         StringBuilder var6;
         try {
            String var3 = this.getConfig().getString("settings.country-api", "http://ip-api.com/json/{ip}?fields=status,country,countryCode");
            int var17 = this.getConfig().getInt("settings.lookup-timeout-ms", 3500);
            URL var5 = new URL(var3.replace("{ip}", var1));
            var2 = (HttpURLConnection)var5.openConnection();
            var2.setConnectTimeout(var17);
            var2.setReadTimeout(var17);
            var2.setRequestMethod("GET");
            var2.setUseCaches(false);
            if (var2.getResponseCode() == 200) {
               var6 = new StringBuilder();
               BufferedReader var7 = new BufferedReader(new InputStreamReader(var2.getInputStream(), StandardCharsets.UTF_8));

               String var8;
               while((var8 = var7.readLine()) != null) {
                  var6.append(var8);
               }

               var7.close();
               String var9 = var6.toString();
               if (!var9.contains("\"status\":\"success\"")) {
                  Matcher var19 = null;
                  return var19;
               }

               Matcher var10 = COUNTRY_CODE.matcher(var9);
               String var11 = var10.find() ? var10.group(1).toUpperCase(Locale.ROOT) : null;
               return var11;
            }

            var6 = null;
         } catch (Exception var15) {
            int var4 = (int)null;
            return var4;
         } finally {
            if (var2 != null) {
               var2.disconnect();
            }

         }

         return var6;
      } else {
         return null;
      }
   }

   private void ensureLanguageFiles() {
      File var1 = new File(this.getDataFolder(), "lang");
      if (!var1.exists()) {
         var1.mkdirs();
      }

      if (!(new File(var1, "tr.yml")).exists()) {
         this.saveResource("lang/tr.yml", false);
      }

      if (!(new File(var1, "eng.yml")).exists()) {
         this.saveResource("lang/eng.yml", false);
      }

   }

   private void loadLanguage() {
      String var1 = this.getConfig().getString("language", "tr").toLowerCase(Locale.ROOT);
      if (!var1.equals("eng")) {
         var1 = "tr";
      }

      this.lang = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "lang/" + var1 + ".yml"));
   }

   private String message(String var1, String var2) {
      String var3 = this.lang == null ? var2 : this.lang.getString(var1, var2);
      return var3.replace("%prefix%", this.getConfig().getString("prefix", "&b&lRProxy &8»"));
   }

   private String safeAddress(String var1) {
      return var1 != null && !var1.isEmpty() ? var1 : "UNKNOWN";
   }

   private String color(String var1) {
      return ChatColor.translateAlternateColorCodes('&', var1);
   }
}
