package com.redecommunity.proxy.punish.commands.checkpunish;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.punish.data.Duration;
import com.redecommunity.proxy.punish.data.Punishment;
import com.redecommunity.proxy.punish.manager.PunishmentManager;

import java.util.List;

/**
 * Created by @SrGutyerrez
 */
public class CheckPunishCommand extends CustomCommand {
    public CheckPunishCommand() {
        super("checkpunir", CommandRestriction.ALL, "helper");
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

        punishments.forEach(Punishment::isActive);

        JSONText jsonText = new JSONText();

        String squareSymbol = "\u2588";

        jsonText.next()
                .text("\n")
                .next()
                .text("§e" + squareSymbol + " Pendente §a" + squareSymbol + " Ativo §c" + squareSymbol + " Finalizado §7" + squareSymbol + " Revogado")
                .next()
                .text("\n\n")
                .next();

        punishments.forEach(punishment -> {
            User staffer = punishment.getStaffer();
            Duration duration = punishment.getDuration();

            jsonText.text(" - ")
                    .next()
                    .text(punishment.getColor() + "[" + punishment.getDate() + "]")
                    .hoverText(
                            "Id: §b#" + punishment.getId() +
                            "\n" +
                            "Aplicada por: §7" + staffer.getPrefix() + staffer.getDisplayName() +
                            "\n\n" +
                            "Data de início: §7" + punishment.getStartDate() +
                            "\n" +
                            "Duração: §7" + duration.getDuration() + " " + duration.getTimeTypeDisplayName() +
                            "\n\n" +
                            "Tipo: §7[" + duration.getPunishType().toString() + "]"
                    ).next()
                    .text(" ")
                    .next()
                    .text(punishment.getColor() + "[" + punishment.getPunishReason().getDisplayName() + "] ")
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

        if (punishments.isEmpty()) {
            jsonText.text("   --/--")
                    .next();
        }

        jsonText.text("\n")
                .next()
                .send(user);
    }
}
