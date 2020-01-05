package com.redecommunity.proxy.twitter.command.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class DisassociateCommand extends CustomArgumentCommand {
    public DisassociateCommand() {
        super(0, "desassociar");
    }

    @Override
    public void onCommand(User user, String[] args) {

    }
}
