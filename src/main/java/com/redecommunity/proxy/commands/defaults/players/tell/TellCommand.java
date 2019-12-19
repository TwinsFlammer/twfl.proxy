package com.redecommunity.proxy.commands.defaults.players.tell;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class TellCommand extends CustomCommand {
    public TellCommand() {
        super("tell", CommandRestriction.IN_GAME, null);
    }

    @Override
    public void onCommand(User user, String[] args) {

    }
}
