package com.lypaka.commandcooldowns.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.commandcooldowns.CommandCooldowns;
import com.lypaka.commandcooldowns.Config.ConfigGetters;
import com.lypaka.commandcooldowns.Config.ConfigManager;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class CommandListener {


    @Listener
    public void onCommandSend (SendCommandEvent event, @Root Player player) throws ObjectMappingException {

        String command = event.getCommand();
        LocalDateTime now = LocalDateTime.now();

        if (!player.hasPermission(ConfigGetters.getCommandBypassPermission())) {

            Map<String, String> commandMap = ConfigManager.getConfigNode(0, "Commands").getValue(new TypeToken<Map<String, String>>() {});

            if (commandMap.containsKey(command)) {

                if (ConfigGetters.getPlayerMap(player).containsKey(command)) {

                    LocalDateTime timer = LocalDateTime.parse(ConfigGetters.getPlayerMap(player).get(command));
                    if (timer.isBefore(now)) {

                        event.setCancelled(true);
                        player.sendMessage(Text.of(TextColors.GOLD, "[", TextColors.DARK_RED, "CommandCooldowns", TextColors.GOLD, "] ", TextColors.WHITE, "You cannot use this command for another " + makeTimeReadable(timer.toString())));

                    } else {

                        setCooldownTimer(now, commandMap.get(command), player, command);

                    }

                }

            }

        }

    }


    private static String makeTimeReadable (String node) {
        LocalDateTime nodeTime = LocalDateTime.parse(node);
        LocalDateTime time = LocalDateTime.now();

        if (!nodeTime.isAfter(time)) return "Expired";

        Duration duration = Duration.between(time, nodeTime);
        return printSeconds(duration.getSeconds());
    }

    private static String printSeconds (long seconds) {
        StringBuilder timeString = new StringBuilder();
        if (timeString.length() != 0 || seconds >= 86400) timeString.append(seconds / 86400).append(" days, ");
        if (timeString.length() != 0 || seconds >= 3600) timeString.append(seconds % 86400 / 3600).append(" hours, ");
        if (timeString.length() != 0 || seconds >= 60) timeString.append(seconds % 3600 / 60).append(" minutes, ");
        timeString.append(seconds % 60).append(" seconds");
        return timeString.toString();
    }

    private static void setCooldownTimer (LocalDateTime time, String cooldown, Player player, String command) throws ObjectMappingException {

        String[] cooldownTimer = cooldown.split(" ");
        int timeAmount = Integer.parseInt(cooldownTimer[0]);
        LocalDateTime then = null;
        String units = cooldownTimer[1];

        if (units.equalsIgnoreCase("Seconds")) {
            then = time.plusSeconds(timeAmount);
        }
        if (units.equalsIgnoreCase("Minutes")) {
            then = time.plusMinutes(timeAmount);
        }
        if (units.equalsIgnoreCase("Hours")) {
            then = time.plusHours(timeAmount);
        }
        if (units.equalsIgnoreCase("Days")) {
            then = time.plusDays(timeAmount);
        }
        if (units.equalsIgnoreCase("Weeks")) {
            then = time.plusWeeks(timeAmount);
        }
        if (units.equalsIgnoreCase("Months")) {
            then = time.plusMonths(timeAmount);
        }
        if (units.equalsIgnoreCase("Years")) {
            then = time.plusYears(timeAmount);
        }

        ConfigGetters.getPlayerMap(player).put(command, then.toString());
        ConfigManager.savePlayer(player.getUniqueId());

    }

}
