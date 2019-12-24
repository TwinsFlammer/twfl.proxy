package com.redecommunity.proxy.commands.defaults.staff;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.proxy.Proxy;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by @SrGutyerrez
 */
public class StaffListCommand extends CustomCommand {
    public StaffListCommand() {
        super("staff", CommandRestriction.ALL, "ajudante");
    }

    @Override
    public void onCommand(User user, String[] strings) {
        JSONText jsonText = new JSONText();

        List<User> users = Proxy.getUsers()
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(user1 -> user1.hasGroup("Helper"))
                        .sorted((user1, user2) -> user2.getHighestGroup().getPriority().compareTo(user1.getHighestGroup().getPriority()))
                        .collect(Collectors.toList());

        jsonText.next()
                .text("\n")
                .next()
                .text("§eEquipe de moderação online (" + users.size() + "):")
                .next()
                .text("\n");

        users.forEach(user1 -> {
            Server server = user1.getServer();

            jsonText.next()
                    .text("\n")
                    .next()
                    .text("  §f- " + (user1.getId().equals(user.getId()) ? "*" : "") + user1.getPrefix() + user1.getDisplayName() + " ")
                    .next()
                    .text(" ")
                    .next()
                    .text("§7[" + server.getDisplayName() + "]")
                    .hoverText("§fUsuários conectados a esse servidor: §7" + server.getPlayerCount() +
                            "\n" +
                            "\n" +
                            "§e* Clique para conectar a este servidor!"
                    )
                    .clickRunCommand("/server conectar " + server.getDisplayName());
        });

        jsonText.next()
                .text("\n")
                .next()
                .send(user);
    }
}
