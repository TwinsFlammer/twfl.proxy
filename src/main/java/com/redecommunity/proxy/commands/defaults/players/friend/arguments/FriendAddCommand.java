package com.redecommunity.proxy.commands.defaults.players.friend.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.commands.defaults.players.friend.FriendCommand;

/**
 * Created by @SrGutyerrez
 */
public class FriendAddCommand extends CustomArgumentCommand {
    public FriendAddCommand() {
        super(0, "add");
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

        if (user.isFriend(user1) && user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.already_friend")
            );
            return;
        }

        if (user.isFriend(user1) && !user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.already_have_invited_friend")
            );
            return;
        }

        if (user1.isFriend(user) && !user.isFriend(user1)) {
            user.sendMessage(
                    language.getMessage("friends.already_have_an_friend_request")
            );
            return;
        }

        user.addFriend(user1);

        user.sendMessage(
                String.format(
                        language.getMessage("friends.friend_request_send"),
                        user1.getDisplayName()
                )
        );
        user1.sendMessage(
                String.format(
                        language.getMessage("friends.friend_request_received"),
                        user.getDisplayName()
                )
        );
    }
}
