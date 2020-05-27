package br.com.twinsflammer.proxy.commands.defaults.players.friend.arguments;

import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.group.dao.UserGroupDao;
import br.com.twinsflammer.common.shared.permissions.user.group.data.UserGroup;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
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

        UserGroupDao userGroupDao = new UserGroupDao();

        List<User> users = user.getFriends()
                .stream()
                .sorted((friendId1, friendId2) -> UserManager.getUser(friendId2).isOnline().compareTo(UserManager.getUser(friendId1).isOnline()))
                .distinct()
                .map(friendId -> {
                    User user1 = UserManager.getUser(friendId);

                    HashMap<String, Object> keys = Maps.newHashMap();

                    keys.put("user_id", keys);

                    List<UserGroup> userGroups = Lists.newArrayList(
                            userGroupDao.findAll(keys)
                    );

                    user1.setGroups(userGroups);

                    return user1;
                })
                .filter(user1 -> user1.isFriend(user))
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
