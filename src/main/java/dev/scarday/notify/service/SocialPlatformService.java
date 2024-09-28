package dev.scarday.notify.service;

import lombok.AllArgsConstructor;
import lombok.val;
import dev.scarday.notify.Notify;
import dev.scarday.notify.social.Social;
import dev.scarday.notify.social.discord.DiscordImpl;
import dev.scarday.notify.social.telegram.TelegramImpl;
import dev.scarday.notify.social.vk.VkImpl;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@AllArgsConstructor
public class SocialPlatformService {

    Notify plugin;

    private void registerPlatforms(List<Social> list) {
        for (val social : list) {
            plugin.getLogger()
                    .info(String.format("Social '%s' is successfully registered!", social.getName()));
        }
    }

    public List<Social> registerAllPlatforms() {
        List<Social> social = Arrays.asList(
                new VkImpl(plugin),
                new TelegramImpl(plugin),
                new DiscordImpl(plugin)
        );

        social = social.stream()
                .filter(socials -> socials.isConnected() && socials.isEnabled())
                .toList();

        registerPlatforms(social);

        return social;
    }
}
