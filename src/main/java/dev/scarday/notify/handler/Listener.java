package dev.scarday.notify.handler;

import lombok.val;
import dev.scarday.notify.Notify;
import dev.scarday.notify.social.MessageBuilder;
import dev.scarday.notify.social.discord.EmbedBuilder;
import dev.scarday.notify.util.MessageUtility;
import org.bukkit.event.EventHandler;
import ru.overwrite.protect.bukkit.api.*;

import java.util.List;

public class Listener implements org.bukkit.event.Listener {

    private final Notify instance;

    public Listener(Notify instance) {
        this.instance = instance;
    }

    @EventHandler
    public void ServerProtectorCaptureEvent(ServerProtectorCaptureEvent e) {
        val message = joinToString(instance.getConfig().getStringList("messages.captured-player"))
                .replace("%player%", e.getPlayer().getName())
                .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress());

        val color = instance.getConfig().getInt("settings.ds.color.yellow");

        instance.getNotificationService()
                .sendMessagePlatforms(MessageBuilder.builder()
                        .message(message)
                        .embedBuilder(EmbedBuilder
                                .builder()
                                .message(message)
                                .color(color)
                                .build())
                        .build());
    }

    @EventHandler
    public void ServerProtectorLogoutEvent(ServerProtectorLogoutEvent e) {
        val message = joinToString(instance.getConfig().getStringList("messages.logout-player"))
                .replace("%player%", e.getPlayer().getName())
                .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress());

        val color = instance.getConfig().getInt("settings.ds.color.yellow");

        instance.getNotificationService()
                .sendMessagePlatforms(MessageBuilder.builder()
                        .message(message)
                        .embedBuilder(EmbedBuilder.builder()
                                .message(message)
                                .color(color)
                                .build())
                        .build());
    }

    @EventHandler
    public void ServerProtectorPasswordEnterEvent(ServerProtectorPasswordEnterEvent e) {
        val message = joinToString(instance.getConfig().getStringList("messages.enter-password"))
                .replace("%player%", e.getPlayer().getName())
                .replace("%ip%", e.getPlayer().getAddress().getAddress().getHostAddress())
                .replace("%password%", e.getEnteredPassword());

        val color = instance.getConfig().getInt("settings.ds.color.yellow");

        instance.getNotificationService()
                .sendMessagePlatforms(MessageBuilder.builder()
                        .message(message)
                        .embedBuilder(EmbedBuilder.builder()
                                .message(message)
                                .color(color)
                                .build())
                        .build());
    }

    @EventHandler
    public void ServerProtectorPasswordFailEvent(ServerProtectorPasswordFailEvent e) {
        val attempts = MessageUtility
                .declineAttempts(e.getAttempts(),
                        instance.getConfig().getStringList("messages.attempts")
                );

        val message = joinToString(instance.getConfig().getStringList("messages.fail-auth"))
                .replace("%player%", e.getPlayer().getName())
                .replace("%ip%", e.getPlayer().getAddress().getHostName())
                .replace("%attempts%", attempts);

        val color = instance.getConfig().getInt("settings.ds.color.red");

        instance.getNotificationService()
                .sendMessagePlatforms(MessageBuilder.builder()
                        .message(message)
                        .embedBuilder(EmbedBuilder.builder()
                                .color(color)
                                .message(message)
                                .build())
                        .build());
    }

    @EventHandler
    public void ServerProtectorPasswordFailEvent(ServerProtectorPasswordSuccessEvent e) {
        val message = joinToString(instance.getConfig().getStringList("messages.success-auth"))
                .replace("%player%", e.getPlayer().getName())
                .replace("%ip%", e.getPlayer().getAddress().getHostName());

        val color = instance.getConfig().getInt("settings.ds.color.green");

        instance.getNotificationService()
                .sendMessagePlatforms(MessageBuilder.builder()
                        .message(message)
                        .embedBuilder(EmbedBuilder.builder()
                                .color(color)
                                .message(message)
                                .build())
                        .build());
    }


    public String joinToString(List<String> stringList) {
        return String.join("\n", stringList);
    }

}
