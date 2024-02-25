package me.scarday.notify.http;

import me.scarday.notify.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;


public class DiscordHttp {
    private final Main instance;

    private final String url;

    public DiscordHttp(String url, Main instance) {
        this.instance = instance;
        this.url = url;
    }

    public void sendMessage(int color, String message) {
        CompletableFuture.runAsync(() -> {
            try {
                URL webhookUrl = new URL(url);

                HttpURLConnection connection = (HttpURLConnection) webhookUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject embed = new JSONObject();
                embed.put("title", instance.getName());
                embed.put("description", message);
                embed.put("color", color);

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
                    if (errorStream != null) {
                        StringBuilder jsonResponse = new StringBuilder();
                        String inputLine;
                        while ((inputLine = errorStream.readLine()) != null) {
                            jsonResponse.append(inputLine);
                        }
                        instance.getLogger().info("Произошла ошибка при отправке сообщения: " + jsonResponse);
                        errorStream.close();
                    }
                }
                connection.disconnect();
            } catch (NullPointerException e) { // работает - не трож
            } catch (Exception e) {
                instance.getLogger().info("Произошла ошибка при отправке сообщения: " + e);
            }
        });
    }

}
