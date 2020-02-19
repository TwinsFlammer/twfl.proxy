package com.redefocus.proxy.commands.defaults.staff.server;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.api.bungeecord.util.JSONText;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.server.manager.ServerManager;
import com.redefocus.proxy.commands.defaults.staff.server.arguments.ServerConnectCommand;

/**
 * Created by @SrGutyerrez
 */
public class ServerCommand extends CustomCommand {
    public static final String COMMAND_NAME = "server";

    public ServerCommand() {
        super(ServerCommand.COMMAND_NAME, CommandRestriction.IN_GAME, "helper");

        this.addArgument(
                new ServerConnectCommand()
        );
    }

    @Override
    public void onCommand(User user, String[] strings) {
        JSONText jsonText = new JSONText();

        jsonText.next()
                .text("\n")
                .next()
                .text("§eLista de servidores disponível (" + ServerManager.getServers().size() + "):")
                .next()
                .text("\n");

        ServerManager.getServers().forEach(server -> {
            jsonText.next()
                    .text("\n")
                    .next()
                    .text("  §f- ")
                    .next()
                    .text(server.inMaintenance() ? "§c[Man.] " : "")
                    .next()
                    .text(server.getStatusColor() + server.getDisplayName() + " ")
                    .hoverText(
                            "§e" + server.getDisplayName() +
                            "\n\n" +
                            server.getDescription() +
                            "\n\n" +
                            "§fJogadores online: §7" + server.getPlayerCount() +
                            "\n" +
                            "§fEstado atual: " + server.getStatus() +
                            "\n" +
                            "§fMáximo de jogadores: §7" + server.getSlots() +
                            "\n" +
                            "§fDescrição: §7" + server.getDescription())
                    .next()
                    .text("§7[Clique para conectar]")
                    .clickRunCommand("/server conectar " + server.getName());
        });

        jsonText.next()
                .text("\n")
                .next()
                .send(user);
    }
}
