package br.com.twinsflammer.proxy.commands.defaults.staff.account.arguments;

import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

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
