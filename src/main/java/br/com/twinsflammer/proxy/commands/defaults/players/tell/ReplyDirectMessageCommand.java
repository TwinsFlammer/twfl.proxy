package br.com.twinsflammer.proxy.commands.defaults.players.tell;

import br.com.twinsflammer.proxy.commands.defaults.players.tell.manager.DirectMessageManager;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;

/**
 * Created by @SrGutyerrez
 */
public class ReplyDirectMessageCommand extends CustomCommand {
    public ReplyDirectMessageCommand() {
        super("r", CommandRestriction.IN_GAME, GroupNames.DEFAULT);
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
