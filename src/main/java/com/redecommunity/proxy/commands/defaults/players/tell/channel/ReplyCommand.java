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
public class ReplyCommand extends CustomCommand {
    public ReplyCommand() {
        super("r", CommandRestriction.IN_GAME, null);
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length == 0) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            "<mensagem>"
                    )
            );
            return;
        }

        Integer targetId = TellManager.getDirectMessageId(user.getId());

        if (targetId == null) {
            user.sendMessage(
                    language.getMessage("tell.messages.no_direct_message_open")
            );
            return;
        }

        User user1 = UserManager.getUser(targetId);

        TellManager.sendMessage(
                user,
                user1,
                args
        );
    }
}
