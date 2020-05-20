package br.com.twinsflammer.proxy.commands.defaults.staff;

import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.proxy.Proxy;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by @SrGutyerrez
 */
public class StaffListCommand extends CustomCommand {
    public StaffListCommand() {
        super("staff", CommandRestriction.ALL, GroupNames.HELPER);
    }

    @Override
    public void onCommand(User user, String[] strings) {
        JSONText jsonText = new JSONText();

        List<User> users = Proxy.getUsers()
                .stream()
                .filter(Objects::nonNull)
                .filter(User::isOnline)
                .filter(User::isLogged)
                .filter(user1 -> user1.hasGroup("helper"))
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
                    .text((user1.getId().equals(user.getId()) ? " §f* " : " §f- ") + user1.getPrefix() + user1.getDisplayName() + " ")
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
