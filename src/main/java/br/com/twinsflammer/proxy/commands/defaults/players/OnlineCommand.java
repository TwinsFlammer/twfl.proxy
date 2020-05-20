package br.com.twinsflammer.proxy.commands.defaults.players;

import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.proxy.Proxy;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;

/**
 * Created by @SrGutyerrez
 */
public class OnlineCommand extends CustomCommand {
    public OnlineCommand() {
        super("online", CommandRestriction.ALL, GroupNames.DEFAULT);
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (user.isConsole()) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.online_players.to_console"),
                            Proxy.getUsers().size()
                    )
            );
        } else {
            Server server = user.getServer();

            user.sendMessage(
                    String.format(
                            language.getMessage("messages.online_players.to_player"),
                            Proxy.getUsers().size(),
                            server.getPlayerCount()
                    )
            );
        }
    }
}
