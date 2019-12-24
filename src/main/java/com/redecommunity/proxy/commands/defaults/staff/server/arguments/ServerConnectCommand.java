package com.redecommunity.proxy.commands.defaults.staff.server.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class ServerConnectCommand extends CustomArgumentCommand {
    public ServerConnectCommand() {
        super(0, "conectar");
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}
