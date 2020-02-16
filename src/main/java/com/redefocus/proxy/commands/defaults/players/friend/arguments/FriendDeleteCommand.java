package com.redefocus.proxy.commands.defaults.players.friend.arguments;

import com.redefocus.api.bungeecord.commands.CustomArgumentCommand;
import com.redefocus.common.shared.friend.storage.FriendStorage;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.proxy.commands.defaults.players.friend.FriendCommand;

/**
 * Created by @SrGutyerrez
 */
public class FriendDeleteCommand extends CustomArgumentCommand {
    public FriendDeleteCommand() {
        super(0, "deletar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            FriendCommand.COMMAND_NAME,
                            "deletar <usuário>"
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

        if (!user.isFriend(user1) && user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.has_not_accepted_invited_at_now")
            );
            return;
        }

        if (user.isFriend(user1) && !user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.isn\'t_friend")
            );
            return;
        }

        user.removeFriend(user1);
        user1.removeFriend(user);

        FriendStorage friendStorage = new FriendStorage();

        friendStorage.delete(
                "user_id",
                user.getId(),
                "friend_id",
                user1.getId()
        );
        friendStorage.delete(
                "user_id",
                user1.getId(),
                "friend_id",
                user.getId()
        );

        user.sendMessage(
                String.format(
                        language.getMessage("friends.deleted"),
                        user1.getDisplayName()
                )
        );
    }
}
