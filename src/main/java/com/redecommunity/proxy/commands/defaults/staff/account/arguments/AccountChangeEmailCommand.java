package com.redecommunity.proxy.commands.defaults.staff.account.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.permissions.user.data.User;

/**
 * Created by @SrGutyerrez
 */
public class AccountChangeEmailCommand extends CustomArgumentCommand {
    public AccountChangeEmailCommand() {
        super(0, "changeemail");
    }

    @Override
    public void onCommand(User user, String[] strings) {
    }
}
