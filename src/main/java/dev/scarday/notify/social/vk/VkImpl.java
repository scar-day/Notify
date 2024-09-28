package dev.scarday.notify.social.vk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.scarday.notify.Notify;
import dev.scarday.notify.social.MessageBuilder;
import dev.scarday.notify.social.Social;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VkImpl implements Social {

    private final Notify instance;

    private final List<Integer> ids;

    private final String token;

    public VkImpl(Notify instance) {
        this.instance = instance;
        ids = instance.getConfig().getIntegerList("settings.vk.chat-id");
        token = instance.getConfig().getString("settings.vk.token");
    }

    public void sendMessage(MessageBuilder builder) {
        CompletableFuture.runAsync(() -> {
            for (Integer peerId : ids) {
                try {
                    String urlString = "https://api.vk.com/method/messages.send?access_token=" + token + "&message=" + URLEncoder.encode(builder.getMessage(), StandardCharsets.UTF_8) + "&peer_id=" + peerId + "&random_id=0&v=5.199";
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
        return true;
    }

    @Override
    public String getName() {
        return "VK";
    }

    @Override
    public boolean isEnabled() {
        return instance.getConfig().getBoolean("settings.vk.enable", false);
    }
}
