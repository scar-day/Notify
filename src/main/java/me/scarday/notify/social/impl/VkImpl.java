package me.scarday.notify.social.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

public class VkImpl implements Social {

    private final Main instance;

    private final List<Integer> ids;

    private final String token;

    public VkImpl(Main instance) {
        this.instance = instance;
        ids = instance.getConfig().getIntegerList("settings.vk.chat-id");
        token = instance.getConfig().getString("settings.vk.token");
    }

    public void sendMessage(Builder builder) {
        CompletableFuture.runAsync(() -> {
            for (Integer peerId : ids) {
                try {
                    String urlString = "https://api.vk.com/method/messages.send?access_token=" + token + "&message=" + URLEncoder.encode(builder.getMessage(), "UTF-8") + "&peer_id=" + peerId + "&random_id=0&v=5.199";
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

    public boolean isConnected() {
        if (token.isEmpty()) {
            instance.getLogger().info("Похоже, вы не ввели токен.");
            return false;
        }

        Gson gson = new Gson();

        try {
            String urlString = "https://api.vk.com/method/groups.getById?access_token=" + token + "&groupId=0&v=5.199";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection == null) {
                instance.getLogger().info("Не удалось установить соединение.");
                return false;
            }

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    jsonResponse.append(inputLine);
                }

                in.close();

                JsonObject json = gson.fromJson(jsonResponse.toString(), JsonObject.class);

                if (json.has("error")) {
                    int error_code = json.getAsJsonObject("error").get("error_code").getAsInt();

                    if (error_code == 5) {
                        instance.getLogger().info("Произошла ошибка! Похоже вы ввели неверный токен от группы.");
                        return false;
                    }
                }
            } else {
                instance.getLogger().info("Ошибка в запросе. Код ответа: " + responseCode);
                return false;
            }
            connection.disconnect();
            return true;
        } catch (IOException e) {
            instance.getLogger().info("Произошла ошибка при отправке запроса: " + e);
            return false;
        }
    }
}
