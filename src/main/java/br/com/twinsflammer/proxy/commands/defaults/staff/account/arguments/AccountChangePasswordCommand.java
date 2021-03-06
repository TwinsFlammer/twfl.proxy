package br.com.twinsflammer.proxy.commands.defaults.staff.account.arguments;

import com.google.common.collect.Maps;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.dao.UserDao;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class AccountChangePasswordCommand extends CustomArgumentCommand {
    public AccountChangePasswordCommand() {
        super(0, "changepass");
    }

    @Override
    public void onCommand(User user, String[] args) {
        if (!user.hasGroup(GroupNames.MANAGER)) return;

        Language language = user.getLanguage();

        if (args.length != 2) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            "account",
                            "changepass <usuário> <nova senha>"
                    )
            );
            return;
        }

        String username = args[0];
        String password = args[1];

        User user1 = UserManager.getUser(username);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        String hashedPassword = Helper.hash(password);

        user1.setPassword(hashedPassword);

        HashMap<String, String> keys = Maps.newHashMap();

        keys.put("password", hashedPassword);

        UserDao userDao = new UserDao();

        userDao.update(
                keys,
                "id",
                user1.getId()
        );

        user.sendMessage(
                String.format(
                        language.getMessage("account.password_changed"),
                        user1.getDisplayName()
                )
        );
    }
}
