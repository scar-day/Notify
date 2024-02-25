package me.scarday.notify;

import me.scarday.notify.handler.Listener;
import me.scarday.notify.http.TelegramHttp;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.overwrite.protect.bukkit.ServerProtectorManager;

public final class Main extends JavaPlugin {

    public TelegramHttp tg;
    public ServerProtectorManager api;
    @Override
    public void onEnable() {
        api = (ServerProtectorManager) Bukkit.getPluginManager().getPlugin("UltimateServerProtector");

        if (getConfig().getBoolean("settings.tg.enable")) {
            tg = new TelegramHttp(getConfig().getStringList("settings.tg.chat-id"), getConfig().getString("settings.tg.token"), this);

            getLogger().info("Интеграция с Telegram включена");
        } else {
            getLogger().info("Интеграция с Telegram выключена");
        }

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new Listener(this), this);
    }

    public ServerProtectorManager getApi() {
        return api;
    }

    @Override
    public void onDisable() {
    }
}
