package com.redecommunity.proxy.commands.defaults.staff.server;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.manager.ServerManager;

/**
 * Created by @SrGutyerrez
 */
public class ServerCommand extends CustomCommand {
    public ServerCommand() {
        super("server", CommandRestriction.IN_GAME, "helper");
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
                    .text(server.getStatusColor() + "")
                    .next()
                    .text(server.getStatus() == 3 ? "[Man.]" : "")
                    .next()
                    .text(server.getDisplayName() + " ")
                    .hoverText(
                            "§fJogadores online: §7" + server.getPlayerCount() +
                            "§fDescrição: §7" + server.getDescription()
                    )
                    .next()
                    .text("§7[Clique para conectar]");
        });

        jsonText.next()
                .text("\n")
                .send(user);
    }
}
