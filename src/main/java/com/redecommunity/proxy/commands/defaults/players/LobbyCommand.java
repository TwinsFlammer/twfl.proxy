package com.redecommunity.proxy.commands.defaults.players;

import com.redecommunity.proxy.Proxy;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.server.data.Server;

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
