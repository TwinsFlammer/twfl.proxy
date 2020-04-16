package com.redecommunity.proxy.commands.defaults.staff.server;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.permissions.group.GroupNames;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.common.shared.server.manager.ServerManager;
import com.redecommunity.proxy.commands.defaults.staff.server.arguments.ServerConnectCommand;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Created by @SrGutyerrez
 */
public class ServerCommand extends CustomCommand {
    public static final String COMMAND_NAME = "server";

    public ServerCommand() {
        super(ServerCommand.COMMAND_NAME, CommandRestriction.IN_GAME, GroupNames.HELPER);

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

        ServerManager.getServers()
                .stream()
                .sorted(Comparator.comparing(server -> !server.isOnline()))
                .sorted(Comparator.comparing(Server::getName))
                .forEach(server -> {
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
