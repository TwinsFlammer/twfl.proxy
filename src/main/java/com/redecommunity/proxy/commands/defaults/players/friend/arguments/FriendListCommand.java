package com.redecommunity.proxy.commands.defaults.players.friend.arguments;

import com.redecommunity.api.bungeecord.commands.CustomArgumentCommand;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Helper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @SrGutyerrez
 */
public class FriendListCommand extends CustomArgumentCommand {
    public FriendListCommand() {
        super(0, "listar");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        String prePage = args.length == 1 ? args[0] : "1";

        if (!Helper.isInteger(prePage)) {
            user.sendMessage(
                    language.getMessage("friends.invalid_page")
            );
            return;
        }

        Integer page = Integer.parseInt(prePage);

        JSONText jsonText = new JSONText();

        List<User> users = user.getFriends()
                .stream()
                .sorted((friendId1, friendId2) -> UserManager.getUser(friendId2).isOnline().compareTo(UserManager.getUser(friendId1).isOnline()))
                .distinct()
                .map(UserManager::getUser)
                .collect(Collectors.toList());

        final Integer perPage = 10;
        Double pagesNumber = (users.size() / perPage.doubleValue());

        Integer pages = (int) Math.ceil(pagesNumber);

        if (page < 1 || page > pages + 1) {
            user.sendMessage(
                    language.getMessage("friends.invalid_page")
            );
            return;
        }

        jsonText.next()
                .text("\n")
                .next()
                .text("§eLista de amigos - " + page + "/" + (pages == 0 ? 1 : pages))
                .next()
                .text("\n\n")
                .next();

        if (users.size() < perPage) {
            users.forEach(user1 -> {
                jsonText.text("  ")
                        .next()
                        .text(user1.getPrefix())
                        .next()
                        .text(user1.getDisplayName())
                        .next()
                        .text("§7 - ")
                        .next()
                        .text(user1.isOnline() ? "§aOnline §7(" + user1.getServer().getDisplayName() + ")" : "§cOffline")
                        .next()
                        .text("\n")
                        .next();
            });
        } else {
            Integer size = perPage * page;

            Integer count = size - users.size();

            users.subList((page == 1 ? 0 : page * 10 - 10), (page == 1 ? 10 : size - count))
                    .forEach(user1 -> {
                        jsonText.text(" - ")
                                .next()
                                .text(user1.getPrefix() + user1.getDisplayName())
                                .next()
                                .text("§7 - ")
                                .next()
                                .text(user1.isOnline() ? "§aOnline §7(" + user1.getServer().getDisplayName() + ")" : "§cOffline")
                                .next()
                                .text("\n")
                                .next();
                    });
        }

        if (users.isEmpty())
            jsonText.text("   --/--")
                    .next()
                    .text("\n")
                    .next();

        jsonText.send(user);
    }
}
