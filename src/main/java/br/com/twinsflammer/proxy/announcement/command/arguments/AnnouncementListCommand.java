package br.com.twinsflammer.proxy.announcement.command.arguments;

import com.google.common.collect.Lists;
import br.com.twinsflammer.proxy.announcement.dao.AnnouncementDao;
import br.com.twinsflammer.proxy.announcement.data.Announcement;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

import java.util.List;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementListCommand extends CustomArgumentCommand {
    public AnnouncementListCommand() {
        super(0, "listar");
    }

    @Override
    public void onCommand(User user, String[] strings) {
        JSONText jsonText = new JSONText();

        jsonText.next()
                .text("\n")
                .next()
                .text("§eLista de anúncios do servidor:")
                .next()
                .text("\n\n")
                .next();

        AnnouncementDao announcementDao = new AnnouncementDao();

        List<Announcement> announcements = Lists.newArrayList(announcementDao.findAll());

        announcements.forEach(announcement -> {
            jsonText.text("  ")
                    .next()
                    .text("§d#" + announcement.getId())
                    .next()
                    .text("§f - ")
                    .next()
                    .text(announcement.getTitle())
                    .next()
                    .text(" ")
                    .next()
                    .text("§7[Passe o mouse p/ ver a mensagem]")
                    .hoverText(announcement.getMessage())
                    .next()
                    .text(" ")
                    .next()
                    .text(announcement.isActive() ? "§aAtivado" : "§cDesativado")
                    .next();
        });

        if (announcements.isEmpty()) {
            jsonText.text("  §cNão há anúncios cadastrados")
                    .next();
        }

        jsonText.text("\n")
                .next()
                .send(user);
    }
}
