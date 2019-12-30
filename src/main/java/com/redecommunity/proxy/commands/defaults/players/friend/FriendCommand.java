package com.redecommunity.proxy.commands.defaults.players.friend;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.commands.defaults.players.friend.arguments.*;

/**
 * Created by @SrGutyerrez
 */
public class FriendCommand extends CustomCommand {
    public static final String COMMAND_NAME = "amigo";

    public FriendCommand() {
        super(FriendCommand.COMMAND_NAME, CommandRestriction.IN_GAME, "default");

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
