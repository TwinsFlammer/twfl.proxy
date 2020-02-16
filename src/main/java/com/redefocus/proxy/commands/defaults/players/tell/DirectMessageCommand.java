package com.redefocus.proxy.commands.defaults.players.tell;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.preference.Preference;
import com.redefocus.proxy.commands.defaults.players.tell.manager.DirectMessageManager;

/**
 * Created by @SrGutyerrez
 */
public class DirectMessageCommand extends CustomCommand {
    public DirectMessageCommand() {
        super("tell", CommandRestriction.IN_GAME, "default");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length <= 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário> <mensagem>"
                    )
            );
            return;
        }

        User user1 = UserManager.getUser(args[0]);

        DirectMessageManager.sendMessage(
                user,
                user1,
                args,
                DirectMessageManager.MessageType.DIRECT_MESSAGE
        );
    }
}
