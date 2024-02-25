package me.scarday.notify.handler;

import me.scarday.notify.Main;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.overwrite.protect.bukkit.api.ServerProtectorCaptureEvent;
import ru.overwrite.protect.bukkit.api.ServerProtectorPasswordEnterEvent;
import ru.overwrite.protect.bukkit.api.ServerProtectorPasswordFailEvent;
import ru.overwrite.protect.bukkit.api.ServerProtectorPasswordSuccessEvent;

import java.util.List;

public class Listener implements org.bukkit.event.Listener {

    private final Main instance;

    public Listener(Main instance) {
        this.instance = instance;
    }

    @EventHandler
    public void ServerProtectorCaptureEvent(ServerProtectorCaptureEvent e) {
        if (instance.tg != null) {
            instance.tg.sendMessage(joinToString(instance.getConfig().getStringList("messages.joined-player")).replace("%player%", e.getPlayer().getName()).replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress()));
        }
    }

    @EventHandler
    public void ServerProtectorPasswordEnterEvent(ServerProtectorPasswordEnterEvent e) {
        if (instance.tg != null) {
            instance.tg.sendMessage(joinToString(instance.getConfig().getStringList("messages.enter-password")).replace("%player%", e.getPlayer().getName()).replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress()).replace("%password%", e.getEnteredPassword()));
        }
    }

    @EventHandler
    public void ServerProtectorPasswordFailEvent(ServerProtectorPasswordFailEvent e) {
        if (instance.tg != null) {
            instance.tg.sendMessage(joinToString(instance.getConfig().getStringList("messages.fail-auth")).replace("%player%", e.getPlayer().getName()).replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress()).replace("%attempts%", String.valueOf(e.getAttempts())));
        }
    }

    @EventHandler
    public void ServerProtectorPasswordSuccessEvent(ServerProtectorPasswordSuccessEvent e) {
        if (instance.tg != null) {
            instance.tg.sendMessage(joinToString(instance.getConfig().getStringList("messages.success-auth")).replace("%player%", e.getPlayer().getName()).replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress()));
        }
    }

    public String joinToString(List<String> stringList) {
        return String.join("\n", stringList);
    }

}
