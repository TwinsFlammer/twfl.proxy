package com.redecommunity.proxy.announcement.manager;

import com.google.common.collect.Lists;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.proxy.announcement.dao.AnnouncementDao;
import com.redecommunity.proxy.announcement.data.Announcement;
import net.md_5.bungee.api.ProxyServer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementManager {
    public static final String CHANNEL_NAME = "announcement_channel";

    private static List<Announcement> announcements = Lists.newArrayList();

    public AnnouncementManager() {
        AnnouncementDao announcementDao = new AnnouncementDao();

        Set<Announcement> announcements = announcementDao.findAll();

        AnnouncementManager.announcements.addAll(announcements);

        Common.getInstance().getScheduler().scheduleWithFixedDelay(
                () -> {
                    if (AnnouncementManager.announcements.isEmpty()) return;

                    JSONText jsonText = new JSONText();

                    jsonText.next()
                            .text("\n")
                            .next()
                            .text("§eInformações recentes úteis:")
                            .next()
                            .text("\n\n")
                            .next();

                    AnnouncementManager.announcements.forEach(announcement -> {
                        jsonText.text("§e * ")
                                .next()
                                .text(announcement.getTitle())
                                .next()
                                .text(" ")
                                .next()
                                .text(announcement.getMessage());

                        if (announcement.getUrl() != null)
                            jsonText.clickOpenURL(announcement.getUrl().toExternalForm());

                        jsonText.next();
                    });

                    ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                        User user = UserManager.getUser(proxiedPlayer.getUniqueId());

                        jsonText.send(user);
                    });
                },
                0,
                15,
                TimeUnit.MINUTES
        );
    }

    public static List<Announcement> getAnnouncements() {
        return AnnouncementManager.announcements;
    }

    public static Announcement getAnnouncement(Integer id) {
        return AnnouncementManager.announcements
                .stream()
                .filter(announcement -> announcement.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Announcement toAnnouncement(ResultSet resultSet) throws SQLException, IOException {
        String resultSetURL = resultSet.getString("url");

        URL url = resultSetURL == null || resultSetURL.isEmpty() ? null : new URL(resultSetURL);

        return new Announcement(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("message"),
                url,
                resultSet.getBoolean("active")
        );
    }
}
