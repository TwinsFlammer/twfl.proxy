package br.com.twinsflammer.proxy.commands.defaults.players;

import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.proxy.Proxy;
import br.com.twinsflammer.api.bungeecord.commands.CustomCommand;
import br.com.twinsflammer.api.bungeecord.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class LobbyCommand extends CustomCommand {
    public final static HashMap<Integer, Long> IN_VALIDATION = Maps.newHashMap();

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

        if (user.isEnabled(Preference.LOBBY_PROTECTION)) {
            if (!LobbyCommand.IN_VALIDATION.containsKey(user.getId()) || LobbyCommand.IN_VALIDATION.get(user.getId()) < System.currentTimeMillis()) {
                user.sendMessage("§aVocê está com a proteção do /lobby ativada, digite o comando novamente para ir para o saguão.");

                LobbyCommand.IN_VALIDATION.put(user.getId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
                return;
            }

            LobbyCommand.IN_VALIDATION.remove(user.getId());
        }

        user.connect(server);
    }
}
