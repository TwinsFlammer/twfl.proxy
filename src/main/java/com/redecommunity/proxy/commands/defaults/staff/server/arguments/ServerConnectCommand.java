package com.redecommunity.proxy.commands.defaults.staff.server.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.proxy.commands.defaults.staff.server.ServerCommand;

/**
 * Created by @SrGutyerrez
 */
public class ServerConnectCommand extends CustomArgumentCommand {
    public ServerConnectCommand() {
        super(0, "conectar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            ServerCommand.COMMAND_NAME,
                            "conectar <name>"
                    )
            );
            return;
        }

        String targetServerName = args[0];

        Server server = ServerManager.getServer(targetServerName);

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
        }

        if (!server.isAccessible() && !user.hasGroup("manager")) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.inaccessible_server")
            );
            return;
        }

        user.sendMessage(
                String.format(
                        language.getMessage("messages.default_commands.server.connecting_to"),
                        server.getName()
                )
        );

        if (user.getServer().isSimilar(server)) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.already_connected")
            );
            return;
        }

        try {
            wait(2L);
            user.connect(server);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
