package com.redecommunity.proxy.commands.defaults.players.friend.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.friend.database.FriendDatabase;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.commands.defaults.players.friend.FriendCommand;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class FriendClearCommand extends CustomArgumentCommand {
    public FriendClearCommand() {
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
                            "clear <período em dias>"
                    )
            );
            return;
        }

        String preTime = args[0];

        if (!Helper.isInteger(preTime)) {
            user.sendMessage(
                    language.getMessage("friends.invalid_period")
            );
            return;
        }

        Long period = System.currentTimeMillis() + TimeUnit.DAYS.toMicros(Long.parseLong(preTime));

        List<Integer> friends = user.getFriends();

        if (friends.isEmpty()) {
            user.sendMessage(
                    language.getMessage("friends.empty_list")
            );
            return;
        }

        Iterator<Integer> usersId = friends.iterator();

        FriendDatabase friendDatabase = new FriendDatabase();

        while (usersId.hasNext()) {
            Integer userId = usersId.next();

            User user1 = UserManager.getUser(userId);

            if (!user1.isFriend(user)) continue;

            if (user1.getLastLogin() < period) {
                usersId.remove();

                friendDatabase.delete(
                        "user_id",
                        user.getId(),
                        "friend_id",
                        userId
                );
                friendDatabase.delete(
                        "user_id",
                        userId,
                        "friend_id",
                        user.getId()
                );
            }
        }

        user.sendMessage(
                String.format(
                        language.getMessage("friends.cleared_list"),
                        period
                )
        );
    }
}
