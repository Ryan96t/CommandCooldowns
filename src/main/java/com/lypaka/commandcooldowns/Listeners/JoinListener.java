package com.lypaka.commandcooldowns.Listeners;

import com.lypaka.commandcooldowns.Config.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class JoinListener {

    @Listener
    public void onJoin (ClientConnectionEvent.Join event, @Root Player player) {

        ConfigManager.loadPlayer(player.getUniqueId());

    }

}
