package me.scarday.notify;

import lombok.Getter;
import me.scarday.notify.handler.Listener;
import me.scarday.notify.social.impl.DiscordImpl;
import me.scarday.notify.social.impl.TelegramImpl;
import me.scarday.notify.social.impl.VkImpl;
import org.bukkit.plugin.java.JavaPlugin;

import ru.overwrite.protect.bukkit.ServerProtectorManager;

@Getter
public final class Main extends JavaPlugin {

    private TelegramImpl tg;
    private VkImpl vk;
    private DiscordImpl discord;

    private ServerProtectorManager api;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        api = (ServerProtectorManager) getServer().getPluginManager().getPlugin("UltimateServerProtector");

        if (getConfig().getBoolean("settings.tg.enable")) {
            tg = new TelegramImpl(this);

            getLogger().info("Интеграция с Telegram включена");
        }

        if (getConfig().getBoolean("settings.vk.enable")) {
            vk = new VkImpl(this);

            if (!vk.isConnected()) {
                vk = null;
            } else {
                getLogger().info("Интеграция с VK успешно включена");
            }
        }

        if (getConfig().getBoolean("settings.ds.enable")) {
            discord = new DiscordImpl(this);

            getLogger().info("Интеграция с Discord включена");
        }

        getServer().getPluginManager().registerEvents(new Listener(this), this);
    }

    @Override
    public void onDisable() {

    }
}
