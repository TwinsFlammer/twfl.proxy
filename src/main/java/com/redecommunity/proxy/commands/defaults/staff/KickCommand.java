package com.redecommunity.proxy.commands.defaults.staff;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Helper;

/**
 * Created by @SrGutyerrez
 */
public class KickCommand extends CustomCommand {
    public KickCommand() {
        super("chutar", CommandRestriction.ALL, "manager");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length == 0) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<mensagem>"
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

        if (!user1.isOnline()) {
            user.sendMessage(
                    language.getMessage("messages.player.player_offline")
            );
        }

        String message = Helper.toMessage(Helper.removeFirst(args));

        if (message.equalsIgnoreCase("")) message = "Nenhum informado";

        user.sendMessage(
                String.format(
                        language.getMessage(""),
                        user1.getDisplayName(),
                        message
                )
        );

        user1.kick(
                String.format(
                        language.getMessage("kick.format"),
                        user.getDisplayName(),
                        message
                )
        );
    }
}
