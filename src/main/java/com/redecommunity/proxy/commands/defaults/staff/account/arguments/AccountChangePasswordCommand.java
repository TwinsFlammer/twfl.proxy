package com.redecommunity.proxy.commands.defaults.staff.account.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class AccountChangePasswordCommand extends CustomArgumentCommand {
    public AccountChangePasswordCommand() {
        super(1, "changepass");
    }

    @Override
    public void onCommand(User user, String[] strings) {

    }
}
