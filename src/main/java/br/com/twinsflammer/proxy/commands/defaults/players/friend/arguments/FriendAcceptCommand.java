package br.com.twinsflammer.proxy.commands.defaults.players.friend.arguments;

import br.com.twinsflammer.proxy.commands.defaults.players.friend.FriendCommand;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;

/**
 * Created by @SrGutyerrez
 */
public class FriendAcceptCommand extends CustomArgumentCommand {
    public FriendAcceptCommand() {
        super(0, "aceitar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            FriendCommand.COMMAND_NAME,
                            "aceitar <usuário>"
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

        if (user1.isFriend(user) && user.isFriend(user1)) {
            user.sendMessage(
                    language.getMessage("friends.already_friend")
            );
            return;
        }

        if (!user1.isFriend(user) && user.isFriend(user1)) {
            user.sendMessage(
                    language.getMessage("friends.already_have_invited_friend")
            );
            return;
        }

        if (!user1.isFriend(user) && user.isFriend(user1)) {
            user.sendMessage(
                    language.getMessage("friends.not_invited_friend_to_you")
            );
            return;
        }

        user.addFriend(user1);

        user.sendMessage(
                String.format(
                        language.getMessage("friends.accepted"),
                        user1.getDisplayName()
                )
        );
        user1.sendMessage(
                String.format(
                        language.getMessage("friends.accepted"),
                        user.getDisplayName()
                )
        );
    }
}
