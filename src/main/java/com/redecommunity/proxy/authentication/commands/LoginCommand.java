package com.redecommunity.proxy.authentication.commands;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.authentication.manager.AuthenticationManager;

/**
 * Created by @SrGutyerrez
 */
public class LoginCommand extends CustomCommand {
    public LoginCommand() {
        super("logar", CommandRestriction.IN_GAME, GroupNames.DEFAULT, new String[] { "login" });
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<senha>"
                    )
            );
            return;
        }

        if (user.getPassword() == null) {
            user.sendMessage(
                    language.getMessage("authentication.not_registered")
            );
            return;
        }

        if (user.isLogged()) {
            user.sendMessage(
                    language.getMessage("authentication.already_logged_in")
            );
            return;
        }

        String password = args[0];

        if (!Helper.compare(password, user.getPassword())) {
            Integer passwordAttempts = AuthenticationManager.getPasswordAttempt(user);

            if (passwordAttempts == null) passwordAttempts = 4;

            passwordAttempts--;

            AuthenticationManager.setPasswordAttempt(
                    user,
                    passwordAttempts
            );

            if (passwordAttempts <= 0) {
                user.kick(
                        language.getMessage("authentication.max_password_attempts_reached")
                );
                AuthenticationManager.getAttempts().remove(user.getId());
                return;
            }

            user.sendMessage(
                    String.format(
                            language.getMessage("authentication.invalid_password"),
                            passwordAttempts
                    )
            );
            return;
        }

        AuthenticationManager.getAttempts().remove(user.getId());

        user.sendMessage(
                language.getMessage("authentication.successfully_logged")
        );

        AuthenticationManager.authenticate(user);
    }
}
