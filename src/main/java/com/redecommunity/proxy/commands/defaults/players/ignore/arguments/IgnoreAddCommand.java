package com.redecommunity.proxy.commands.defaults.players.ignore.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.commands.defaults.players.ignore.IgnoreCommand;

/**
 * Created by @SrGutyerrez
 */
public class IgnoreAddCommand extends CustomArgumentCommand {
    public IgnoreAddCommand() {
        super(0, "add");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            IgnoreCommand.COMMAND_NAME,
                            "add <usuário>"
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

        if (user.isIgnoring(user1)) {
            user.sendMessage(
                    language.getMessage("ignore.already_ignoring")
            );
            return;
        }

        user.ignore(user1);

        user.sendMessage(
                String.format(
                        language.getMessage("ignore.user_ignored"),
                        user1.getDisplayName()
                )
        );
    }
}
