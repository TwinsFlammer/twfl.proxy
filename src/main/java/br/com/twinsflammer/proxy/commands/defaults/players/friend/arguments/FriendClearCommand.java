package br.com.twinsflammer.proxy.commands.defaults.players.friend.arguments;

import br.com.twinsflammer.proxy.commands.defaults.players.friend.FriendCommand;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.friend.storage.FriendStorage;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;

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

        FriendStorage friendStorage = new FriendStorage();

        while (usersId.hasNext()) {
            Integer userId = usersId.next();

            User user1 = UserManager.getUser(userId);

            if (!user1.isFriend(user)) continue;

            if (user1.getLastLogin() < period) {
                usersId.remove();

                friendStorage.delete(
                        "user_id",
                        user.getId(),
                        "friend_id",
                        userId
                );
                friendStorage.delete(
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
