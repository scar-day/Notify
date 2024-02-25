package me.scarday.notify.http;

import me.scarday.notify.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VkHttp {
    private final Main instance;

    private final List<Integer> ids;

    private final String token;

    public VkHttp(List<Integer> ids, String token, Main instance) {
        this.instance = instance;
        this.ids = ids;
        this.token = token;
    }

    public void sendMessage(String text) {
        CompletableFuture.runAsync(() -> {
            for (Integer peerId : ids) {
                try {
                    String urlString = "https://api.vk.com/method/messages.send?access_token=" + token + "&message=" + URLEncoder.encode(text, "UTF-8") + "&peer_id=" + peerId + "&random_id=0&v=5.199";
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();

                    if (responseCode !=  HttpURLConnection.HTTP_OK) {
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
}
