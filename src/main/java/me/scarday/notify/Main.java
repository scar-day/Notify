package me.scarday.notify;

import me.scarday.notify.handler.Listener;
import me.scarday.notify.http.DiscordHttp;
import me.scarday.notify.http.TelegramHttp;
import me.scarday.notify.http.VkHttp;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.overwrite.protect.bukkit.ServerProtectorManager;

public final class Main extends JavaPlugin {

    private TelegramHttp tg;

    private VkHttp vk;

    private DiscordHttp discord;

    private ServerProtectorManager api;
    @Override
    public void onEnable() {
        saveDefaultConfig();

        api = (ServerProtectorManager) Bukkit.getPluginManager().getPlugin("UltimateServerProtector");

        if (getConfig().getBoolean("settings.tg.enable")) {
            tg = new TelegramHttp(getConfig().getIntegerList("settings.tg.chat-id"), getConfig().getString("settings.tg.token"), this);

            getLogger().info("Интеграция с Telegram включена");
        }

        if (getConfig().getBoolean("settings.vk.enable")) {
            vk = new VkHttp(getConfig().getIntegerList("settings.vk.chat-id"), getConfig().getString("settings.vk.token"), this);
            getLogger().info("Интеграция с VK включена");
        }

        if (getConfig().getBoolean("settings.ds.enable")) {
            discord = new DiscordHttp(getConfig().getString("settings.ds.url"), this);

            getLogger().info("Интеграция с Discord включена");
        }

        getServer().getPluginManager().registerEvents(new Listener(this), this);
    }

    public ServerProtectorManager getApi() {
        return api;
    }

    public VkHttp getVk() {
        return vk;
    }

    public TelegramHttp getTg() {
        return tg;
    }

    public DiscordHttp getDiscord() {
        return discord;
    }

    @Override
    public void onDisable() {
    }
}
