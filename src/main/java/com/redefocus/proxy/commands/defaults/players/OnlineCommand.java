package com.redefocus.proxy.commands.defaults.players;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.enums.CommandRestriction;
import com.redefocus.common.shared.language.enums.Language;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.proxy.Proxy;

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
