package br.com.twinsflammer.proxy.shorter.command.arguments;

import br.com.twinsflammer.proxy.shorter.manager.ShortedURLManager;
import com.google.common.collect.Maps;
import br.com.twinsflammer.proxy.shorter.command.ShortCommand;
import br.com.twinsflammer.proxy.shorter.dao.ShortedURLDao;
import br.com.twinsflammer.proxy.shorter.data.ShortedURL;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class ShortEnableCommand extends CustomArgumentCommand {
    public ShortEnableCommand() {
        super(0, "enable");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            ShortCommand.COMMAND_NAME,
                            "<nome>"
                    )
            );
        }

        String name = args[0];

        ShortedURL shortedURL = ShortedURLManager.getShortedURL(name);

        if (shortedURL == null) {
            user.sendMessage(
                    language.getMessage("shortener.unknown_shorted_url")
            );
            return;
        }

        ShortedURLDao shortedURLDao = new ShortedURLDao();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("active", true);

        shortedURLDao.update(keys, "id", shortedURL.getId());

        shortedURL.setActive(true);

        user.sendMessage(
                language.getMessage("shortener.disabled")
        );
    }
}
