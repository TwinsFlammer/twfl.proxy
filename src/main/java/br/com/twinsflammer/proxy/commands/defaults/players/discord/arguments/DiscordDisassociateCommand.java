package br.com.twinsflammer.proxy.commands.defaults.players.discord.arguments;

import com.google.common.collect.Maps;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.dao.UserDao;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class DiscordDisassociateCommand extends CustomArgumentCommand {
    public DiscordDisassociateCommand() {
        super(0, "desassociar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (user.getDiscordId() == 0 || user.getDiscordId() == null) {
            user.sendMessage(
                    language.getMessage("discord.not_associated")
            );
            return;
        }

        user.setDiscordId(null);

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("discord_id", null);

        UserDao userDao = new UserDao();

        userDao.update(
                keys,
                "id",
                user.getId()
        );

        user.sendMessage(
                language.getMessage("discord.disassociated")
        );
    }
}
