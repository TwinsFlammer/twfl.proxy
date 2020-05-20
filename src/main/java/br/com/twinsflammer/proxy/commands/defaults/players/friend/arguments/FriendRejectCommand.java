package br.com.twinsflammer.proxy.commands.defaults.players.friend.arguments;

import br.com.twinsflammer.proxy.commands.defaults.players.friend.FriendCommand;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;

/**
 * Created by @SrGutyerrez
 */
public class FriendRejectCommand extends CustomArgumentCommand {
    public FriendRejectCommand() {
        super(0, "rejeitar");
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

        if (user.isFriend(user1) && user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.already_friend")
            );
            return;
        }

        if (user.isFriend(user1) && !user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.not_invited_friend_to_you")
            );
            return;
        }

        user1.removeFriend(user);

        user.sendMessage(
                String.format(
                        language.getMessage("friends.rejected"),
                        user1.getDisplayName()
                )
        );

        user1.sendMessage(
                String.format(
                        language.getMessage("friends.invite_rejected"),
                        user.getDisplayName()
                )
        );
    }
}
