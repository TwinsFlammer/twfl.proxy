package com.redefocus.proxy.commands.defaults.staff;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.group.data.Group;
import com.redefocus.common.shared.permissions.group.manager.GroupManager;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.proxy.Proxy;

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
                "%s§f: %s",
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
