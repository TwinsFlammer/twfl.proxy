package com.redecommunity.proxy.commands.defaults.players.ignore.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class IgnoreAddCommand extends CustomArgumentCommand {
    public IgnoreAddCommand() {
        super(0, "cancelar");
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}
