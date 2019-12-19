package com.redecommunity.proxy.commands.defaults.players.tell.channel;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.commands.defaults.players.tell.manager.TellManager;

/**
 * Created by @SrGutyerrez
 */
public class TellCommand extends CustomCommand {
    public TellCommand() {
        super("tell", CommandRestriction.IN_GAME, null);
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length <= 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            "<usuário> <mensagem>"
                    )
            );
            return;
        }

        User user1 = UserManager.getUser(args[0]);

        TellManager.sendMessage(
                user,
                user1,
                args
        );
    }
}
