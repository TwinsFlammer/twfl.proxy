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
public class LobbyCommand extends CustomCommand {
    public LobbyCommand() {
        super("lobby", CommandRestriction.IN_GAME, "default");
    }

    @Override
    public void onCommand(User user, String[] strings) {
        Language language = user.getLanguage();

        Server server = Proxy.getLobby();

        if (server == null) {
            user.sendMessage(
                    language.getMessage("messages.default_commands.not_have_lobby_live")
            );
            return;
        }

        user.connect(server);
    }
}
