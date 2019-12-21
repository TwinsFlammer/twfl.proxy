package com.redecommunity.proxy.commands.defaults.players;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.data.Server;
import com.redecommunity.proxy.Proxy;

/**
 * Created by @SrGutyerrez
 */
public class OnlineCommand extends CustomCommand {
    public OnlineCommand() {
        super("online", CommandRestriction.ALL, "default");
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
