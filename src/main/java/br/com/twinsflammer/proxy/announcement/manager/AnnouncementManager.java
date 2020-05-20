package br.com.twinsflammer.proxy.announcement.manager;

import br.com.twinsflammer.proxy.announcement.dao.AnnouncementDao;
import com.google.common.collect.Lists;
import br.com.twinsflammer.proxy.announcement.data.Announcement;
import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import net.md_5.bungee.api.ProxyServer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementManager {
    public AnnouncementManager() {
        AnnouncementDao announcementDao = new AnnouncementDao();

        Common.getInstance().getScheduler().scheduleWithFixedDelay(
                () -> {
                    Set<Announcement> announcements = announcementDao.findAll();

                    List<Announcement> announcements1 = Lists.newArrayList(announcements)
                            .stream()
                            .filter(Announcement::isActive)
                            .collect(Collectors.toList());

                    if (announcements1.isEmpty()) return;

                    JSONText jsonText = new JSONText();

                    jsonText.next()
                            .text("\n")
                            .next()
                            .text("§eInformações recentes úteis:")
                            .next()
                            .text("\n\n")
                            .next();

                    announcements1.forEach(announcement -> {
                        jsonText.text("§e * ")
                                .next()
                                .text(announcement.getTitle())
                                .next()
                                .text(" ")
                                .next()
                                .text(announcement.getMessage());

                        if (announcement.getUrl() != null)
                            jsonText.clickOpenURL(announcement.getUrl().toExternalForm());

                        jsonText.next()
                                .text("\n")
                                .next()
                                .text("§7 [Ver mais]");
                    });

                    ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                        User user = UserManager.getUser(proxiedPlayer.getUniqueId());

                        if (user.isLogged()) jsonText.send(user);
                    });
                },
                0,
                15,
                TimeUnit.MINUTES
        );
    }

    public static Announcement getAnnouncement(Integer id) {
        AnnouncementDao announcementDao = new AnnouncementDao();

        return announcementDao.findOne("id", id);
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
