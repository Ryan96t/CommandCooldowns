package com.lypaka.commandcooldowns;

import com.google.inject.Inject;
import com.lypaka.commandcooldowns.Config.ConfigManager;
import com.lypaka.commandcooldowns.Listeners.CommandListener;
import com.lypaka.commandcooldowns.Listeners.JoinListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

@Plugin(
        id = "commandcooldowns",
        name = "CommandCooldowns",
        description = "Simple Sponge plugin for assigning cooldowns to commands",
        authors = {
                "Lypaka",
                "Teck"
        }
)
public class CommandCooldowns {

    @Inject
    public Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path dir;

    @Inject
    private PluginContainer container;

    public static CommandCooldowns instance;



    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        logger.info("Loading cool down timers for commands!");
        instance = this;
        ConfigManager.setup(dir);
        Sponge.getEventManager().registerListeners(this, new CommandListener());
        Sponge.getEventManager().registerListeners(this, new JoinListener());

    }

    @Listener
    public void onReload (GameReloadEvent event) {

        ConfigManager.load();
        logger.info("Reloaded command cool down map!");

    }



    public static PluginContainer getContainer() {
        return instance.container;
    }

    public static Logger getLogger() {
        return instance.logger;
    }
}
