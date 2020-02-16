package com.redefocus.proxy.commands.defaults.players.tell;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.proxy.commands.defaults.players.tell.manager.DirectMessageManager;

/**
 * Created by @SrGutyerrez
 */
public class ReplyDirectMessageCommand extends CustomCommand {
    public ReplyDirectMessageCommand() {
        super("r", CommandRestriction.IN_GAME, "default");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length == 0) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<mensagem>"
                    )
            );
            return;
        }

        Integer targetId = DirectMessageManager.getDirectMessageId(user.getId());

        if (targetId == null) {
            user.sendMessage(
                    language.getMessage("tell.messages.no_direct_message_open")
            );
            return;
        }

        User user1 = UserManager.getUser(targetId);

        DirectMessageManager.sendMessage(
                user,
                user1,
                args,
                DirectMessageManager.MessageType.REPLY_MESSAGE
        );
    }
}
