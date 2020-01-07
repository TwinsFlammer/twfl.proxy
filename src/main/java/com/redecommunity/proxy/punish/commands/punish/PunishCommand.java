package com.redecommunity.proxy.punish.commands.punish;

import com.google.common.collect.Maps;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.punish.dao.PunishmentDao;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.PunishReason;
import com.redecommunity.proxy.punish.data.Punishment;
import com.redecommunity.proxy.punish.manager.PunishReasonManager;
import com.redecommunity.proxy.punish.manager.PunishmentManager;

import java.util.*;
import java.util.concurrent.TimeUnit;
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

        if (args.length == 1) {
            String targetName = args[0];

            User user1 = UserManager.getUser(targetName);

            if (user1 == null) {
                user.sendMessage(
                        language.getMessage("messages.player.invalid_player")
                );
                return;
            }

            JSONText jsonText = new JSONText();

            List<PunishReason> punishReasons = PunishReasonManager.getPunishReasons(user.getHighestGroup());

            jsonText.next()
                    .text("\n")
                    .next()
                    .text("§eLista de infração disponíveís (" + punishReasons.size() + ")")
                    .next()
                    .text("\n\n");

            punishReasons.forEach(punishReason -> {
                jsonText.next()
                        .text(" - " + punishReason.getDisplayName());

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("§e")
                        .append(punishReason.getDisplayName())
                        .append("\n\n")
                        .append("§f")
                        .append(punishReason.getDescription())
                        .append("\n\n")
                        .append("§fGrupo mínimo: ")
                        .append(punishReason.getGroup().getFancyPrefix())
                        .append("\n")
                        .append("§fRedes: §7SURVIVAL")
                        .append("\n\n");

                List<Duration> durations = punishReason.getDurations();

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

                jsonText.hoverText(stringBuilder.toString())
                        .clickSuggest("/punir " + user1.getDisplayName() + " " + punishReason.getName() + " ")
                        .next()
                        .text("\n");
            });

            if (punishReasons.isEmpty()) jsonText.text("   --/--")
                    .next()
                    .text("\n")
                    .next();

            jsonText.send(user);
            return;
        } else if (args.length >= 2) {
            String targetName = args[0];
            String motiveName = args[1];
            String proof = args.length >= 3 ? args[2] : null;
            Boolean hidden = args.length == 4 && args[3].equals("-s");

            User user1 = UserManager.getUser(targetName);

            if (user1 == null) {
                user.sendMessage(
                        language.getMessage("messages.player.invalid_player")
                );
                return;
            }

            PunishReason punishReason = PunishReasonManager.getPunishReason(motiveName);

            if (punishReason == null) {
                user.sendMessage(
                        language.getMessage("punishment.motive_does_not_exists")
                );
                return;
            }

            if (user.getHighestGroup().getPriority() < punishReason.getGroup().getPriority()) {
                user.sendMessage(
                        String.format(
                                language.getMessage("messages.default_commands.invalid_group"),
                                punishReason.getGroup().getFancyPrefix()
                        )
                );
                return;
            }

            if (!user.hasGroup("manager") && !Helper.isURLValid(proof)) {
                user.sendMessage(
                        language.getMessage("punishment.invalid_proof_url")
                );
                return;
            }

            HashMap<String, Object> keys = Maps.newHashMap();

            keys.put("user_id", user1.getId());
            keys.put("reason_id", punishReason.getId());
            keys.put("status", true);

            List<Punishment> punishments = PunishmentManager.getPunishments(user1);

            Long minTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMicros(15);

            Optional<Punishment> optionalPunishment = punishments
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(punishment -> punishment.getTime() >= minTime)
                    .findAny();

            if (optionalPunishment.isPresent()) {
                user.sendMessage(
                        language.getMessage("punishment.user_already_punished_with_this_motive_in_moments_ago")
                );
                return;
            }

            Punishment punishment = PunishmentManager.generatePunishment(
                    user,
                    user1,
                    punishReason,
                    proof,
                    hidden
            );

            PunishmentDao punishmentDao = new PunishmentDao();

            punishmentDao.insert(punishment);

            punishment.broadcast();

            user.sendMessage(
                    String.format(
                            language.getMessage("punishment.punishment_applied"),
                            user1.getDisplayName()
                    )
            );
            return;
        } else {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário> <motivo> <prova>"
                    )
            );
            return;
        }
    }
}
