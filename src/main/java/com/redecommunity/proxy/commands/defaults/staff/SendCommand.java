package com.redecommunity.proxy.commands.defaults.staff;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;

/**
 * Created by @SrGutyerrez
 */
public class SendCommand extends CustomCommand {
    public SendCommand() {
        super("send", CommandRestriction.ALL, "administrator");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 2) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário> <servidor>"
                    )
            );
            return;
        }

        String targetName = args[0];
        String serverName = args[1];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        if (!user1.isOnline()) {
            user.sendMessage(
                    language.getMessage("messages.player.player_offline")
            );
        }

        Server server = ServerManager.getServer(serverName);

        if (server == null) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.unknown_server")
            );
            return;
        }

        if (server.isRestarting()) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.restarting_server")
            );
            return;
        }

        if (!server.isOnline()) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.offline_server")
            );
            return;
        }

        if (!server.isAccessible() && !user.hasGroup("manager")) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.inaccessible_server")
            );
            return;
        }

        if (user1.getServer().isSimilar(server)) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.server.user_already_connected"),
                            user1.getDisplayName()
                    )
            );
            return;
        }


        user.sendMessage(
                String.format(
                        language.getMessage("messages.default_commands.server.sending_player_to_server"),
                        user.getDisplayName(),
                        server.getDisplayName()
                )
        );

        user1.connect(server);
    }
}
