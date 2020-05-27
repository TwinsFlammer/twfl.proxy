package br.com.twinsflammer.proxy.commands.defaults.staff;

import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.group.data.Group;
import br.com.twinsflammer.common.shared.permissions.group.manager.GroupManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.proxy.Proxy;

/**
 * Created by @SrGutyerrez
 */
public class AnnounceCommand extends CustomCommand {
    public AnnounceCommand() {
        super("alerta", CommandRestriction.ALL, GroupNames.MANAGER);
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

        String message = Helper.toMessage(args);

        String broadcastMessage = String.format(
                "\n§r%s§f: %s\n§r",
                user.isConsole() ? "§e[!]" : user.getPrefix() + user.getDisplayName(),
                message
        );

        Group group = GroupManager.getGroup("default");

        Proxy.broadcastMessage(
                group,
                broadcastMessage
        );
    }
}
