package com.redecommunity.proxy.commands.defaults.players.ignore.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class IgnoreRemoveCommand extends CustomArgumentCommand {
    public IgnoreRemoveCommand() {
        super(0, "cancelar");
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}
