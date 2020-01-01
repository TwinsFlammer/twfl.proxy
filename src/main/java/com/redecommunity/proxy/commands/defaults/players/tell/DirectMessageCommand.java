package com.redecommunity.proxy.commands.defaults.players.tell;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.preference.Preference;
import com.redecommunity.proxy.commands.defaults.players.tell.manager.DirectMessageManager;

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

        if (user.isSimilar(user1)) {
            user.sendMessage(
                    language.getMessage("tell.messages.cant_send_to_yourself")
            );
            return;
        }

        if (!user1.isToggle(Preference.PRIVATE_MESSAGE) && !user.hasGroup("manager")) {
            user.sendMessage(
                    language.getMessage("tell.messages.target_disabled_private_messages")
            );
            return;
        }

        DirectMessageManager.sendMessage(
                user,
                user1,
                args,
                DirectMessageManager.MessageType.DIRECT_MESSAGE
        );
    }
}
