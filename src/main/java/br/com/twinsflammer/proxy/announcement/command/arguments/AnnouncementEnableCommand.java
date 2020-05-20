package br.com.twinsflammer.proxy.announcement.command.arguments;

import com.google.common.collect.Maps;
import br.com.twinsflammer.proxy.announcement.dao.AnnouncementDao;
import br.com.twinsflammer.proxy.announcement.data.Announcement;
import br.com.twinsflammer.proxy.announcement.manager.AnnouncementManager;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.proxy.announcement.command.AnnouncementCommand;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementEnableCommand extends CustomArgumentCommand {
    public AnnouncementEnableCommand() {
        super(0, "ativar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            AnnouncementCommand.COMMAND_NAME,
                            "ativar <id>"
                    )
            );
            return;
        }

        String preId = args[0];

        if (!Helper.isInteger(preId)) {
            user.sendMessage(
                    language.getMessage("announcement.invalid_id")
            );
            return;
        }

        Integer id = Integer.parseInt(preId);

        Announcement announcement = AnnouncementManager.getAnnouncement(id);

        if (announcement == null) {
            user.sendMessage(
                    language.getMessage("announcement.invalid_announcement")
            );
            return;
        }

        announcement.setActive(true);

        AnnouncementDao announcementDao = new AnnouncementDao();

        HashMap<String, Boolean> keys = Maps.newHashMap();

        keys.put("active", true);

        announcementDao.update(
                keys,
                "id",
                id
        );

        user.sendMessage(
                String.format(
                        language.getMessage("announcement"),
                        user.getId()
                )
        );
    }
}
