package com.redefocus.proxy.commands.defaults.staff.account.arguments;

import com.redefocus.api.bungeecord.commands.CustomArgumentCommand;
import com.redefocus.common.shared.permissions.user.data.User;

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
