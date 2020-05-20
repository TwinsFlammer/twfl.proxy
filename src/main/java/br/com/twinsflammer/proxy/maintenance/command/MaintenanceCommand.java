package br.com.twinsflammer.proxy.maintenance.command;

import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.proxy.maintenance.factory.MaintenanceFactory;

import java.util.Arrays;

/**
 * Created by @SrGutyerrez
 */
public class MaintenanceCommand extends CustomCommand {
    public MaintenanceCommand() {
        super("manutenção", CommandRestriction.ALL, GroupNames.MANAGER);
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
        String switchModeString = switchMode ? "ativada" : "desligada";

        if (serverName.equalsIgnoreCase("geral")) {
            if (switchMode && MaintenanceFactory.inMaintenance() || !switchMode && !MaintenanceFactory.inMaintenance()) {
                user.sendMessage(
                        String.format(
                                language.getMessage("maintenance.already_in_status"),
                                switchMode
                        )
                );
                return;
            }

            MaintenanceFactory.setMaintenance(switchMode);
            user.sendMessage(
                    String.format(
                            language.getMessage("maintenance.server_status_changed"),
                            "geral",
                            switchModeString
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

        if (switchMode && server.inMaintenance() || !switchMode && !server.inMaintenance()) {
            user.sendMessage(
                    String.format(
                            language.getMessage("maintenance.already_in_status"),
                            switchModeString
                    )
            );
            return;
        }

        Integer oldStatus = server.getOldStatus() == null ? 0 : server.getOldStatus();

        server.setStatus(switchMode ? 3 : oldStatus);

        user.sendMessage(
                String.format(
                        language.getMessage("maintenance.server_status_changed"),
                        server.getDisplayName(),
                        switchModeString
                )
        );
    }
}
