package dev.scarday.notify.social.discord;

import lombok.Builder;
import lombok.Data;

import java.awt.*;

@Builder
@Data
public class EmbedBuilder {
    String message;
    int color;
}
