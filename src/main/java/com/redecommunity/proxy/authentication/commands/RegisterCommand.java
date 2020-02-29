package com.redecommunity.proxy.authentication.commands;

import com.google.common.collect.Maps;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.dao.UserDao;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.authentication.manager.AuthenticationManager;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class RegisterCommand extends CustomCommand {
    public RegisterCommand() {
        super("registrar", CommandRestriction.IN_GAME, GroupNames.DEFAULT, "register");
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

        if (user.getPassword() != null) {
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

        user.setLogged(true);

        AuthenticationManager.authenticate(user);
    }
}