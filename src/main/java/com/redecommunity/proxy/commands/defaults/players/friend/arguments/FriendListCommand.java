package com.redecommunity.proxy.commands.defaults.players.friend.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class FriendListCommand extends CustomArgumentCommand {
    public FriendListCommand() {
        super(0, "cancelar");
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}
