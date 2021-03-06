package br.com.twinsflammer.proxy.commands.defaults.staff.server.arguments;

import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.proxy.commands.defaults.staff.server.ServerCommand;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;

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
                            "conectar <servidor>"
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

        if (!server.isAccessible() && !user.hasGroup(GroupNames.MANAGER)) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.inaccessible_server")
            );
            return;
        }

        if (user.getServer().isSimilar(server)) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.already_connected")
            );
            return;
        }

        user.sendMessage(
                String.format(
                        language.getMessage("messages.default_commands.server.connecting_to"),
                        server.getDisplayName()
                )
        );

        user.connect(server);
    }
}
