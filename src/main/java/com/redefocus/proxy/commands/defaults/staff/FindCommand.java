package com.redefocus.proxy.commands.defaults.staff;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;

/**
 * Created by @SrGutyerrez
 */
public class FindCommand extends CustomCommand {
    public FindCommand() {
        super("find", CommandRestriction.ALL, GroupNames.MODERATOR);
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

        if (!user1.isOnline()) {
            user.sendMessage(
                    language.getMessage("messages.player.player_offline")
            );
            return;
        }

        user.sendMessage(
                String.format(
                        language.getMessage("find"),
                        user1.getDisplayName(),
                        user1.getServer().getDisplayName()
                )
        );
    }
}