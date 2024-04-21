package me.scarday.notify.social.impl;

import me.scarday.notify.Main;
import me.scarday.notify.social.Builder;
import me.scarday.notify.social.Social;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class DiscordImpl implements Social {
    private final Main plugin;

    private final String url;

    public DiscordImpl(Main plugin) {
        this.plugin = plugin;
        url = plugin.getConfig().getString("settings.ds.url");
    }

    @Override
    public void sendMessage(Builder builder) {
        CompletableFuture.runAsync(() -> {
            try {
                URL webhookUrl = new URL(url);

                HttpURLConnection connection = (HttpURLConnection) webhookUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject embed = new JSONObject();
                embed.put("title", plugin.getName());
                embed.put("description", builder.getMessage());
                embed.put("color", builder.getColor());

                JSONArray embedArray = new JSONArray();
                embedArray.add(embed);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("embeds", embedArray);

                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] requestBody = jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                    outputStream.write(requestBody, 0, requestBody.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    BufferedReader errorStream = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder jsonResponse = new StringBuilder();
                    String inputLine;
                    while ((inputLine = errorStream.readLine()) != null) {
                        jsonResponse.append(inputLine);
                    }
                    plugin.getLogger().info("Произошла ошибка при отправке сообщения: " + jsonResponse);
                    errorStream.close();
                }
                connection.disconnect();
            } catch (IOException e) {
                plugin.getLogger().info("Произошла ошибка при отправке сообщения: " + e);
            }
        });
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
