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
public class DirectMessageCommand extends CustomCommand {
    public DirectMessageCommand() {
        super("tell", CommandRestriction.IN_GAME, GroupNames.DEFAULT);
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
