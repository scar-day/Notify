package dev.scarday.notify.service;

import lombok.AllArgsConstructor;
import dev.scarday.notify.social.MessageBuilder;
import dev.scarday.notify.social.Social;

import java.util.List;

@AllArgsConstructor
public class NotificationService {
    List<Social> socials;
    public void sendMessagePlatforms(MessageBuilder messageBuilder) {
        for (Social social : socials) {
            social.sendMessage(messageBuilder);
        }
    }
}
