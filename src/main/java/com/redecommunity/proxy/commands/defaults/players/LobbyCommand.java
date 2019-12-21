package com.redecommunity.proxy.commands.defaults.players;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class LobbyCommand extends CustomCommand {
    public LobbyCommand() {
        super("lobby", CommandRestriction.IN_GAME, "default");
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}
