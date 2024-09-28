package dev.scarday.notify.social.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.val;
import dev.scarday.notify.Notify;
import dev.scarday.notify.social.MessageBuilder;
import dev.scarday.notify.social.Social;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class DiscordImpl implements Social {
    private Notify plugin;

    private String url;

    public DiscordImpl(Notify plugin) {
        this.plugin = plugin;
        url = plugin.getConfig().getString("settings.ds.url");
    }

    @Override
    public void sendMessage(MessageBuilder builder) {
        CompletableFuture.runAsync(() -> {
            try {
                val webhookUrl = getUrl(url);
                val connection = getHttpURLConnection(builder, webhookUrl);

                val responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val errorStream = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    val jsonResponse = new StringBuilder();
                    String inputLine;
                    while ((inputLine = errorStream.readLine()) != null) {
                        jsonResponse.append(inputLine);
                    }
                    errorStream.close();

                    plugin.getLogger()
                            .info("Произошла ошибка при отправке сообщения: " + jsonResponse);
                }
                connection.disconnect();
            } catch (IOException e) {
                plugin.getLogger()
                        .info("Произошла ошибка при отправке сообщения: " + e);
            }
        });
    }

    private @NotNull HttpURLConnection getHttpURLConnection(MessageBuilder builder, URL webhookUrl) throws IOException {
        val connection = (HttpURLConnection) webhookUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        val embedBuilder = builder.getEmbedBuilder();

        val embed = new JsonObject();
        embed.addProperty("title", plugin.getName());
        embed.addProperty("description", embedBuilder.getMessage());
        embed.addProperty("color", embedBuilder.getColor());

        val embedArray = new JsonArray();
        embedArray.add(embed);

        val jsonBody = new JsonObject();
        jsonBody.add("embeds", embedArray);

        try (OutputStream outputStream = connection.getOutputStream()) {
            val requestBody = jsonBody.toString()
                    .getBytes(StandardCharsets.UTF_8);

            outputStream.write(requestBody, 0, requestBody.length);
        }
        return connection;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public String getName() {
        return "Discord";
    }

    @Override
    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("settings.ds.enable", false);
    }
}
