package dev.scarday.notify.social.telegram;

import lombok.val;
import dev.scarday.notify.Notify;
import dev.scarday.notify.social.MessageBuilder;
import dev.scarday.notify.social.Social;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TelegramImpl implements Social {
    private final Notify plugin;
    private final List<Integer> ids;
    private final String token;

    public TelegramImpl(Notify instance) {
        this.plugin = instance;
        token = instance.getConfig().getString("settings.tg.token");
        ids = instance.getConfig().getIntegerList("settings.tg.chat-id");
    }

    @Override
    public void sendMessage(MessageBuilder builder) {
        CompletableFuture.runAsync(() -> {
            for (Integer chatId : ids) {
                try {
                    val url = getUrl(
                            "https://api.telegram.org/bot" + token + "/sendMessage?text=" +
                                    URLEncoder.encode(builder.getMessage(), StandardCharsets.UTF_8)
                                    + "&chat_id=" + chatId
                    );

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    try {
                        int responseCode = connection.getResponseCode();
                        if (responseCode != HttpURLConnection.HTTP_OK) {
                            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                                StringBuilder jsonResponse = new StringBuilder();
                                String inputLine;

                                while ((inputLine = in.readLine()) != null) {
                                    jsonResponse.append(inputLine);
                                }

                                Bukkit.getLogger()
                                        .info("Произошла ошибка при отправке сообщения: " + jsonResponse);
                            }
                        }
                    } finally {
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    Bukkit.getLogger().info("Произошла ошибка при отправке сообщения: " + e);
                }
            }
        });
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public String getName() {
        return "Telegram";
    }

    @Override
    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("settings.tg.enable", false);
    }
}
