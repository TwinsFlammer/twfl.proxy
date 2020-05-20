package br.com.twinsflammer.proxy.punish.commands.unpunish;

import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.proxy.punish.data.Punishment;
import br.com.twinsflammer.proxy.punish.data.RevokeReason;
import br.com.twinsflammer.proxy.punish.manager.PunishmentManager;
import br.com.twinsflammer.proxy.punish.manager.RevokeReasonManager;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.util.Helper;

import java.util.List;

/**
 * Created by @SrGutyerrez
 */
public class UnPunishCommand extends CustomCommand {
    public UnPunishCommand() {
        super("unpunish", CommandRestriction.IN_GAME, GroupNames.HELPER);
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length == 1) {
            String preId = args[0];

            if (!Helper.isInteger(preId)) {
                user.sendMessage(
                        language.getMessage("punishment.invalid_punishment_id")
                );
                return;
            }

            Integer id = Integer.parseInt(preId);

            Punishment punishment = PunishmentManager.getPunishment(id);

            if (punishment == null) {
                user.sendMessage(
                        language.getMessage("punishment.invalid_punishment")
                );
                return;
            }

            List<RevokeReason> revokeReasons = RevokeReasonManager.getReasons();

            JSONText jsonText = new JSONText();

            jsonText.next()
                    .text("\n")
                    .next()
                    .text("§eMotivos de revogação de punição disponíveis (" + revokeReasons.size() + ")")
                    .next()
                    .text("\n\n")
                    .next();

            revokeReasons.forEach(revokeReason -> {
                jsonText.text(" - ")
                        .next()
                        .text(revokeReason.getDisplayName())
                        .hoverText(
                                "§e" + revokeReason.getDisplayName() +
                                "\n\n" +
                                "§f" + revokeReason.getDescription() +
                                "\n\n" +
                                "§fGrupo mínimo: " + revokeReason.getGroup().getFancyPrefix()
                        )
                        .clickSuggest("/unpunish " + id + " " + revokeReason.getName())
                        .next()
                        .text("\n")
                        .next();
            });

            if (revokeReasons.isEmpty()) jsonText.text("   --/--")
                    .next()
                    .text("\n")
                    .next();

            jsonText.send(user);
            return;
        } else if (args.length == 2) {
            String preId = args[0];
            String revokeReasonName = args[1];

            if (!Helper.isInteger(preId)) {
                user.sendMessage(
                        language.getMessage("punishment.invalid_punishment_id")
                );
                return;
            }

            Integer id = Integer.parseInt(preId);

            Punishment punishment = PunishmentManager.getPunishment(id);

            if (punishment == null) {
                user.sendMessage(
                        language.getMessage("punishment.invalid_punishment")
                );
                return;
            }

            RevokeReason revokeReason = RevokeReasonManager.getRevokeReason(revokeReasonName);

            if (revokeReason == null) {
                user.sendMessage(
                        language.getMessage("punishment.motive_does_not_exists")
                );
                return;
            }

            if (!punishment.isActive()) {
                user.sendMessage(
                        language.getMessage("punishment.already_revoked")
                );
                return;
            }

            if (!punishment.canRevokeBy(user)) {
                user.sendMessage(
                        language.getMessage("punishment.revoke_time_expired")
                );
                return;
            }

            punishment.revoke(
                    user,
                    revokeReason
            );

            user.sendMessage(
                    String.format(
                            language.getMessage("punishment.revoked_punishment"),
                            punishment.getId(),
                            punishment.getUser().getDisplayName()
                    )
            );
            return;
        } else {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<id> <motivo>"
                    )
            );
            return;
        }
    }
}
