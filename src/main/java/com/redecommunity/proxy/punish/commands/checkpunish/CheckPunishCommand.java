package com.redecommunity.proxy.punish.commands.checkpunish;

import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.Punishment;
import com.redecommunity.proxy.punish.data.RevokeReason;
import com.redecommunity.proxy.punish.manager.PunishmentManager;
import com.redecommunity.proxy.punish.manager.RevokeReasonManager;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;

import java.util.List;

/**
 * Created by @SrGutyerrez
 */
public class CheckPunishCommand extends CustomCommand {
    public CheckPunishCommand() {
        super("checkpunir", CommandRestriction.ALL, GroupNames.HELPER);
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário>"
                    )
            );
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        List<Punishment> punishments = PunishmentManager.getPunishments(user1);

        JSONText jsonText = new JSONText();

        String squareSymbol = "\u2588";

        jsonText.next()
                .text("\n")
                .next()
                .text("§e" + squareSymbol + " Pendente §a" + squareSymbol + " Ativo §c" + squareSymbol + " Finalizado §7" + squareSymbol + " Revogado")
                .next()
                .text("\n\n")
                .next();

        punishments.stream()
                .sorted((punishment1, punishment2) -> punishment2.getId().compareTo(punishment1.getId()))
                .forEach(punishment -> {
                    User revoker = punishment.getRevoker();
                    User staffer = punishment.getStaffer();
                    Duration duration = punishment.getDuration();

                    RevokeReason revokeReason = RevokeReasonManager.getRevokeReason(punishment.getRevokeReasonId());

                    jsonText.text(" - ")
                            .next()
                            .text(punishment.getColor() + "[" + punishment.getDate() + "] [" + punishment.getPunishReason().getDisplayName() + "]")
                            .hoverText(
                                    "Id: §b#" + punishment.getId() +
                                            "\n" +
                                            "Aplicada por: §7" + staffer.getPrefix() + staffer.getDisplayName() +
                                            "\n\n" +
                                            "Data de início: §7" + punishment.getStartDate() +
                                            "\n" +
                                            "Duração: §7" + duration.getTimeTypeDisplayName() +
                                            "\n\n" +
                                            "Tipo: §7[" + duration.getPunishType().toString() + "]" +
                                            (
                                                    punishment.isRevoked() ?
                                                            "\n\n" +
                                                                    "Revogada por: " + revoker.getPrefix() + revoker.getDisplayName() +
                                                                    "\n" +
                                                                    "Revogada em: " + punishment.getRevokeDate() +
                                                                    "\n" +
                                                                    "Motivo: " + revokeReason.getDisplayName()
                                                            :
                                                            ""
                                            )
                            ).next()
                            .text(" ")
                            .next()
                            .text("[Prova]");

                    if (punishment.hasValidProof()) jsonText.clickOpenURL(punishment.getProof());

                    jsonText.next()
                            .text(" ")
                            .next()
                            .text("§f[Revogar]")
                            .clickSuggest("/unpunish " + punishment.getId())
                            .hoverText("§7Clique para revogar esta punição")
                            .next()
                            .text("\n")
                            .next();
                });

        if (punishments.isEmpty()) jsonText.text("   --/--")
                .next()
                .text("\n")
                .next();

        jsonText.send(user);
    }
}
