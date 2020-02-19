package com.redefocus.proxy.maintenance.command;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.common.shared.server.manager.ServerManager;
import com.redefocus.proxy.maintenance.factory.MaintenanceFactory;

import java.util.Arrays;

/**
 * Created by @SrGutyerrez
 */
public class MaintenanceCommand extends CustomCommand {
    public MaintenanceCommand() {
        super("manutenção", CommandRestriction.ALL, "manager");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 2) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<on/off> <servidor>"
                    )
            );
            return;
        }

        String[] actions = new String[]{
                "on",
                "off"
        };

        String action = args[0];
        String serverName = args[1];

        if (!Arrays.asList(actions).contains(action)) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<on/off> <servidor>"
                    )
            );
            return;
        }

        Boolean switchMode = action.equals("on");

        if (serverName.equalsIgnoreCase("geral")) {
            MaintenanceFactory.setMaintenance(switchMode);
            user.sendMessage(
                    String.format(
                            language.getMessage("maintenance.server_status_changed"),
                            "geral",
                            switchMode ? "ativada" : "desativada"
                    )
            );
            return;
        }

        Server server = ServerManager.getServer(serverName);

        if (server == null) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.server.unknown_server")
            );
            return;
        }

        server.setStatus(switchMode ? 3 : 0);

        user.sendMessage(
                String.format(
                        language.getMessage("maintenance.server_status_changed"),
                        server.getDisplayName(),
                        switchMode ? "ativada" : "desativada"
                )
        );
    }
}
