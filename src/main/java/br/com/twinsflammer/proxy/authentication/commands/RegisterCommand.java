package br.com.twinsflammer.proxy.authentication.commands;

import com.google.common.collect.Maps;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.dao.UserDao;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.proxy.authentication.manager.AuthenticationManager;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class RegisterCommand extends CustomCommand {
    public RegisterCommand() {
        super("registrar", CommandRestriction.IN_GAME, GroupNames.DEFAULT, new String[] { "register" });
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 2) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<senha> <e-mail>"
                    )
            );
            return;
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty() || user.getEmail() != null && user.getEmail().isEmpty()) {
            user.sendMessage(
                    language.getMessage("authentication.already_registered")
            );
            return;
        }

        String password = args[0];
        String email = args[1];

        if (!Helper.isValidMail(email)) {
            user.sendMessage(
                    language.getMessage("authentication.invalid_mail")
            );
            return;
        }

        if (password.length() < 6) {
            user.sendMessage(
                    language.getMessage("authentication.invalid_requirements")
            );
            return;
        }

        UserDao userDao = new UserDao();

        User user1 = userDao.findOne("email", email);

        if (user1 != null) {
            user.sendMessage(
                    language.getMessage("authentication.email_already_in_use")
            );
            return;
        }

        String hashedPassword = Helper.hash(password);

        user.setPassword(hashedPassword);
        user.setEmail(email);

        HashMap<String, String> keys = Maps.newHashMap();

        keys.put("password", hashedPassword);
        keys.put("email", email);

        userDao.update(
                keys,
                "id",
                user.getId()
        );

        user.sendMessage(
                language.getMessage("authentication.successfully_registered")
        );

        AuthenticationManager.authenticate(user);
    }
}
