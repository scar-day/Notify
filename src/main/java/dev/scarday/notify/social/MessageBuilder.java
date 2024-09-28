package dev.scarday.notify.social;

import lombok.Builder;
import lombok.Data;
import dev.scarday.notify.social.discord.EmbedBuilder;

@Data
@Builder
public class MessageBuilder {
    String message;
    EmbedBuilder embedBuilder;
}
