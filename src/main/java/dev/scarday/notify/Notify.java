package dev.scarday.notify;

import lombok.Getter;
import lombok.val;
import dev.scarday.notify.handler.Listener;
import dev.scarday.notify.service.NotificationService;
import dev.scarday.notify.service.SocialPlatformService;
import org.bukkit.plugin.java.JavaPlugin;

import ru.overwrite.protect.bukkit.ServerProtectorManager;

@Getter
public class Notify extends JavaPlugin {

    ServerProtectorManager api;
    NotificationService notificationService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        api = (ServerProtectorManager) getServer().getPluginManager().getPlugin("UltimateServerProtector");

        val social = new SocialPlatformService(this)
                .registerAllPlatforms();

        notificationService = new NotificationService(social);

        getServer().getPluginManager().registerEvents(new Listener(this), this);
    }

    @Override
    public void onDisable() {

    }
}
