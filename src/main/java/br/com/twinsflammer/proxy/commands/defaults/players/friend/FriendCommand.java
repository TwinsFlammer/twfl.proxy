package br.com.twinsflammer.proxy.commands.defaults.players.friend;

import br.com.twinsflammer.proxy.commands.defaults.players.friend.arguments.*;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class FriendCommand extends CustomCommand {
    public static final String COMMAND_NAME = "amigo";

    public FriendCommand() {
        super(FriendCommand.COMMAND_NAME, CommandRestriction.IN_GAME, GroupNames.DEFAULT);

        this.addArgument(
                new FriendAcceptCommand(),
                new FriendAddCommand(),
                new FriendCancelCommand(),
                new FriendClearCommand(),
                new FriendDeleteAllCommand(),
                new FriendDeleteCommand(),
                new FriendListCommand(),
                new FriendRejectCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        user.sendMessage(
                language.getMessage("friends.usage")
        );
    }
}
