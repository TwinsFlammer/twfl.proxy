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
public class LobbyCommand extends CustomCommand {
    public LobbyCommand() {
        super("lobby", CommandRestriction.IN_GAME, GroupNames.DEFAULT);
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
