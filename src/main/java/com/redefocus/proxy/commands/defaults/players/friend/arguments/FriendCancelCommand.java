package com.redefocus.proxy.commands.defaults.players.friend.arguments;

import com.redefocus.api.bungeecord.commands.CustomArgumentCommand;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.proxy.commands.defaults.players.friend.FriendCommand;

/**
 * Created by @SrGutyerrez
 */
public class FriendCancelCommand extends CustomArgumentCommand {
    public FriendCancelCommand() {
        super(0, "cancelar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            FriendCommand.COMMAND_NAME,
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

        if (!user.isFriend(user1)) {
            user.sendMessage(
                    String.format(
                            language.getMessage("friends.not_invited_friend_to_user"),
                            user1.getDisplayName()
                    )
            );
            return;
        }

        user.removeFriend(user1);

        user.sendMessage(
                String.format(
                        language.getMessage("friends.request_cancelled"),
                        user1.getDisplayName()
                )
        );
    }
}
