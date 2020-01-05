package com.redecommunity.proxy.twitter.command.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class InfoCommand extends CustomArgumentCommand {
    public InfoCommand() {
        super(0, "info");
    }

    @Override
    public void onCommand(User user, String[] args) {

    }
}
