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
public class FriendDeleteAllCommand extends CustomArgumentCommand {
    public FriendDeleteAllCommand() {
        super(0, "deletar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1 || !args[0].equalsIgnoreCase("todos")) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            FriendCommand.COMMAND_NAME,
                            "deletar todos"
                    )
            );
            return;
        }

        FriendStorage friendStorage = new FriendStorage();

        friendStorage.delete(
                "user_id",
                user.getId()
        );

        user.getFriends().forEach(userId -> {
            User user1 = UserManager.getUser(userId);

            user1.removeFriend(user);
        });

        user.getFriends().clear();

        user.sendMessage(
                language.getMessage("friends.deleted_list")
        );
    }
}
