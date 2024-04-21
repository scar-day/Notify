package me.scarday.notify.social.impl;

import me.scarday.notify.Main;
import me.scarday.notify.social.Builder;
import me.scarday.notify.social.Social;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TelegramImpl implements Social {
    private final Main instance;

    private final List<Integer> ids;

    private final String token;

    public TelegramImpl(Main instance) {
        this.instance = instance;
        token = instance.getConfig().getString("settings.tg.token");
        ids = instance.getConfig().getIntegerList("settings.tg.chat-id");
    }


    @Override
    public void sendMessage(Builder builder) {
        CompletableFuture.runAsync(() -> {
            for (Integer chatId : ids) {
                try {
                    String urlString = "https://api.telegram.org/bot" + token + "/sendMessage?text=" + URLEncoder.encode(builder.getMessage(), "UTF-8") + "&chat_id=" + chatId;
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder jsonResponse = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            jsonResponse.append(inputLine);
                        }
                        in.close();

                        instance.getLogger().info("Произошла ошибка при отправке сообщения: " + jsonResponse);
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    instance.getLogger().info("Произошла ошибка при отправке сообщения: " + e);
                }
            }
        });
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
