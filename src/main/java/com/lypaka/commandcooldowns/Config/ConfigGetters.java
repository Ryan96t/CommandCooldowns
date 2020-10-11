package com.lypaka.commandcooldowns.Config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;

public class ConfigGetters {

    public static String getCommandBypassPermission () {

        return ConfigManager.getConfigNode(0, "Settings", "Cooldown-Bypass-Permission").getString();

    }

    public static Map<String, String> getPlayerMap(Player player) throws ObjectMappingException {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Cooldowns").getValue(new TypeToken<Map<String, String>>() {});

    }

}
