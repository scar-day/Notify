package dev.scarday.notify.social;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public interface Social {
    void sendMessage(MessageBuilder builder);

    boolean isConnected();
    boolean isEnabled();

    String getName();

    default URL getUrl(String url) throws MalformedURLException {
        return URI.create(url).toURL();
    }
}
