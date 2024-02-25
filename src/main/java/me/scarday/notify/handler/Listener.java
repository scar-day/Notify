package me.scarday.notify.handler;

import me.scarday.notify.Main;
import me.scarday.notify.util.Utility;
import org.bukkit.event.EventHandler;
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
            instance.tg.sendMessage(joinToString(instance.getConfig().getStringList("messages.captured-player")).replace("%player%", e.getPlayer().getName()).replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress()));
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
        String attempts = Utility.declineAttempts(e.getAttempts(), instance.getConfig().getString("messages.attempt"),
                instance.getConfig().getString("messages.tryings"),
                instance.getConfig().getString("messages.attempts"));
        if (instance.tg != null) {
            instance.tg.sendMessage(joinToString(instance.getConfig().getStringList("messages.fail-auth")).replace("%player%", e.getPlayer().getName()).replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress()).replace("%attempts%", attempts));
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
