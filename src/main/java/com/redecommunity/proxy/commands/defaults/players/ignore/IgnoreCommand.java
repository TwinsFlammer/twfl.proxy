package com.redecommunity.proxy.commands.defaults.players.ignore;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.commands.defaults.players.ignore.arguments.IgnoreAddCommand;
import com.redecommunity.proxy.commands.defaults.players.ignore.arguments.IgnoreListCommand;
import com.redecommunity.proxy.commands.defaults.players.ignore.arguments.IgnoreRemoveCommand;

/**
 * Created by @SrGutyerrez
 */
public class IgnoreCommand extends CustomCommand {
    public IgnoreCommand() {
        super("ignorar", CommandRestriction.IN_GAME, "default");

        this.addArgument(
                new IgnoreAddCommand(),
                new IgnoreListCommand(),
                new IgnoreRemoveCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}