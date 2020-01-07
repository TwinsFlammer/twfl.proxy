package com.redecommunity.proxy.punish.commands.punish;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.PunishMotive;
import com.redecommunity.proxy.punish.manager.PunishMotiveManager;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by @SrGutyerrez
 */
public class PunishCommand extends CustomCommand {
    public PunishCommand() {
        super("punir", CommandRestriction.ALL, "helper");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        // /punir guty HACK https://prova.com -s

        if (args.length == 0 || args.length > 4) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário> <motivo> <prova>"
                    )
            );
            return;
        } else if (args.length == 1) {
            String targetName = args[0];

            User user1 = UserManager.getUser(targetName);

            if (user1 == null) {
                user.sendMessage(
                        language.getMessage("messages.player.invalid_player")
                );
                return;
            }

            JSONText jsonText = new JSONText();

            List<PunishMotive> punishMotives = PunishMotiveManager.getPunishMotives();

            jsonText.next()
                    .text("\n")
                    .next()
                    .text("§eLista de infração disponíveís (" + punishMotives.size() + ")")
                    .next()
                    .text("\n\n");

            punishMotives.forEach(punishMotive -> {
                jsonText.next()
                        .text(" - " + punishMotive.getDisplayName());

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("§e")
                        .append(punishMotive.getDisplayName())
                        .append("\n\n")
                        .append("§f")
                        .append(punishMotive.getDescription())
                        .append("\n\n")
                        .append("§fGrupo mínimo: ")
                        .append(punishMotive.getGroup().getFancyPrefix())
                        .append("\n")
                        .append("§fRedes: §7SURVIVAL")
                        .append("\n\n");

                List<Duration> durations = punishMotive.getDurations();

                IntStream.range(0, durations.size())
                        .forEach(index -> {
                            Duration duration = durations.get(index);

                            stringBuilder.append(index + 1)
                                    .append("º: ")
                                    .append("§f[")
                                    .append(duration.getPunishType().toString())
                                    .append("] ")
                                    .append(duration.getDuration())
                                    .append(" ")
                                    .append(duration.getTimeTypeDisplayName())
                                    .append("\n");
                        });
            });
        }
    }
}
