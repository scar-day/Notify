package me.scarday.notify.handler;

import me.scarday.notify.Main;
import me.scarday.notify.social.Builder;
import me.scarday.notify.util.MessageUtility;
import org.bukkit.event.EventHandler;
import ru.overwrite.protect.bukkit.api.*;

import java.util.List;

public class Listener implements org.bukkit.event.Listener {

    private final Main instance;

    public Listener(Main instance) {
        this.instance = instance;
    }

    @EventHandler
    public void ServerProtectorCaptureEvent(ServerProtectorCaptureEvent e) {
        if (instance.getVk() != null) {
            instance.getVk().sendMessage(
                    Builder.builder()
                            .message(
                                    joinToString(instance.getConfig().getStringList("messages.captured-player"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                            )
                            .build()
            );
        }

        if (instance.getTg() != null) {
            instance.getTg().sendMessage(
                    Builder.builder()
                            .message(
                                    joinToString(instance.getConfig().getStringList("messages.captured-player"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                            )
                            .build()
            );
        }

        if (instance.getDiscord() != null) {
            instance.getDiscord().sendMessage(
                    Builder.builder()
                            .color(instance.getConfig().getInt("settings.ds.color.yellow"))
                            .message(
                                    joinToString(instance.getConfig().getStringList("messages.captured-player"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                            )
                            .build()
            );
        }
    }

    @EventHandler
    public void ServerProtectorLogoutEvent(ServerProtectorLogoutEvent e) {
        if (instance.getVk() != null) {
            instance.getVk().sendMessage(
                    Builder.builder()
                            .message(
                                    joinToString(instance.getConfig().getStringList("messages.logout-player"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                            )
                            .build()
            );
        }

        if (instance.getTg() != null) {
            instance.getTg().sendMessage(
                    Builder.builder()
                                    .message(
                                            joinToString(instance.getConfig().getStringList("messages.logout-player"))
                                                    .replace("%player%", e.getPlayer().getName())
                                                    .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                                    )
                            .build()
            );
        }

        if (instance.getDiscord() != null) {
            instance.getDiscord().sendMessage(
                    Builder.builder()
                            .color(instance.getConfig().getInt("settings.ds.color.yellow"))
                            .message(
                                    joinToString(instance.getConfig().getStringList("messages.logout-player"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                            )
                            .build()
            );
        }
    }

    @EventHandler
    public void ServerProtectorPasswordEnterEvent(ServerProtectorPasswordEnterEvent e) {
        if (instance.getVk() != null) {
            instance.getVk().sendMessage(
                    Builder.builder()
                            .message(
                                    joinToString(instance.getConfig().getStringList("messages.enter-password"))
                                    .replace("%player%", e.getPlayer().getName())
                                    .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                                    .replace("%password%", e.getEnteredPassword())
                            )
                            .build()
            );
        }

        if (instance.getTg() != null) {
            instance.getTg().sendMessage(
                    Builder.builder()
                            .message(joinToString(instance.getConfig().getStringList("messages.enter-password"))
                                    .replace("%player%", e.getPlayer().getName())
                                    .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                                    .replace("%password%", e.getEnteredPassword())
                            )
                            .build()
            );
        }

        if (instance.getDiscord() != null) {
            instance.getDiscord().sendMessage(
                    Builder.builder()
                            .color(instance.getConfig().getInt("settings.ds.color.yellow"))
                            .message(joinToString(instance.getConfig().getStringList("messages.enter-password"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                                            .replace("%password%", e.getEnteredPassword()))
                            .build()
                    );
        }
    }

    @EventHandler
    public void ServerProtectorPasswordFailEvent(ServerProtectorPasswordFailEvent e) {

        String attempts = MessageUtility.declineAttempts(e.getAttempts(), instance.getConfig().getStringList("messages.attempts"));

        if (instance.getVk() != null) {
            instance.getVk().sendMessage(
                    Builder.builder()
                            .message(joinToString(instance.getConfig().getStringList("messages.fail-auth"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getHostName())
                                            .replace("%attempts%", attempts)
                            )
                            .build()
            );
        }

        if (instance.getTg() != null) {
            instance.getTg().sendMessage(
                    Builder.builder()
                            .message(joinToString(instance.getConfig().getStringList("messages.fail-auth"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getHostName()).replace("%attempts%", attempts)
                            )
                            .build()
            );
        }

        if (instance.getDiscord() != null) {
            instance.getDiscord().sendMessage(Builder.builder()
                    .color(instance.getConfig().getInt("settings.ds.color.red"))
                    .message(joinToString(instance.getConfig().getStringList("messages.fail-auth"))
                                    .replace("%player%", e.getPlayer().getName())
                                    .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                                    .replace("%attempts%", attempts))
                    .build()
            );
        }
    }

    @EventHandler
    public void ServerProtectorPasswordFailEvent(ServerProtectorPasswordSuccessEvent e) {
        if (instance.getVk() != null) {
            instance.getVk().sendMessage(
                    Builder.builder()
                            .message(joinToString(instance.getConfig().getStringList("messages.success-auth"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getHostName())
                            )
                            .build()
            );
        }

        if (instance.getTg() != null) {
            instance.getTg().sendMessage(
                    Builder.builder()
                            .message(joinToString(instance.getConfig().getStringList("messages.success-auth"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getHostName())
                            )
                            .build()
            );
        }

        if (instance.getDiscord() != null) {
            instance.getDiscord().sendMessage(
                    Builder.builder()
                            .color(instance.getConfig().getInt("settings.ds.color.green"))
                            .message(joinToString(instance.getConfig().getStringList("messages.success-auth"))
                                            .replace("%player%", e.getPlayer().getName())
                                            .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress()
                                            )
                            )
                            .build()
            );
        }
    }


    public String joinToString(List<String> stringList) {
        return String.join("\n", stringList);
    }

}
