package com.redecommunity.proxy.commands.defaults.players.friend.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class FriendAcceptCommand extends CustomArgumentCommand {
    public FriendAcceptCommand() {
        super(0, "aceitar");
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}
