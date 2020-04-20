package com.redefocus.proxy.commands.defaults.players.friend;

import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.proxy.commands.defaults.players.friend.arguments.*;
import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.proxy.commands.defaults.players.friend.arguments.*;

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
