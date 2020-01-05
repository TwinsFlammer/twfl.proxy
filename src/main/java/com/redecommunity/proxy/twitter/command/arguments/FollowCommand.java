package com.redecommunity.proxy.twitter.command.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class FollowCommand extends CustomArgumentCommand {
    public FollowCommand() {
        super(0, "seguir");
    }

    @Override
    public void onCommand(User user, String[] args) {

    }
}
