package com.redecommunity.proxy.announcement.command.arguments;

import com.google.common.collect.Maps;
import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.announcement.command.AnnouncementCommand;
import com.redecommunity.proxy.announcement.dao.AnnouncementDao;
import com.redecommunity.proxy.announcement.data.Announcement;
import com.redecommunity.proxy.announcement.manager.AnnouncementManager;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementDisableCommand extends CustomArgumentCommand {
    public AnnouncementDisableCommand() {
        super(0, "desativar");
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

        announcement.setActive(false);

        AnnouncementDao announcementDao = new AnnouncementDao();

        HashMap<String, Boolean> keys = Maps.newHashMap();

        keys.put("active", false);

        announcementDao.update(
                keys,
                "id",
                id
        );
    }
}
