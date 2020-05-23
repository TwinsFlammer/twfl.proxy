package br.com.twinsflammer.proxy.punish.commands.checkpunish;

import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.proxy.punish.dao.PunishmentDao;
import br.com.twinsflammer.proxy.punish.data.Duration;
import br.com.twinsflammer.proxy.punish.data.Punishment;
import br.com.twinsflammer.proxy.punish.data.RevokeReason;
import br.com.twinsflammer.proxy.punish.manager.PunishmentManager;
import br.com.twinsflammer.proxy.punish.manager.RevokeReasonManager;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class CheckPunishCommand extends CustomCommand {
    private final String SQUARE_SYMBOL = "\u2588";

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

//        PunishmentDao punishmentDao = new PunishmentDao();
//
//        HashMap<String, Object> keys = Maps.newHashMap();

        Set<Punishment> punishments = PunishmentManager.getPunishments(user);

        JSONText jsonText = new JSONText();

        jsonText.next()
                .text("\n")
                .next()
                .text("§e" + this.SQUARE_SYMBOL + " Pendente §a" + this.SQUARE_SYMBOL + " Ativo §c" + this.SQUARE_SYMBOL + " Finalizado §7" + this.SQUARE_SYMBOL + " Revogado")
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
                                                            "\n" +
                                                            "\n" +
                                                            "Revogada por: " + revoker.getPrefix() + revoker.getDisplayName() +
                                                            "\n" +
                                                            "Revogada em: " + punishment.getRevokeDate() +
                                                            "\n" +
                                                            "Motivo: " + (revokeReason == null ? "Nenhum especificado" : revokeReason.getDisplayName())
                                                            :
                                                            ""
                                            )
                            ).next()
                            .text(" ")
                            .next()
                            .text("[Prova]");

                    if (punishment.hasValidProof())
                        jsonText.hoverText(punishment.getProof())
                                .clickOpenURL(punishment.getProof());

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
