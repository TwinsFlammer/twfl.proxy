package br.com.twinsflammer.proxy.commands.defaults.players.friend.arguments;

import br.com.twinsflammer.api.bungeecord.util.JSONText;
import br.com.twinsflammer.proxy.Proxy;
import br.com.twinsflammer.proxy.commands.defaults.players.friend.FriendCommand;
import br.com.twinsflammer.api.bungeecord.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.proxy.user.data.ProxyUser;

/**
 * Created by @SrGutyerrez
 */
public class FriendAddCommand extends CustomArgumentCommand {
    public FriendAddCommand() {
        super(0, "add");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            FriendCommand.COMMAND_NAME,
                            "add <usuário>"
                    )
            );
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        if (user.isFriend(user1) && user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.already_friend")
            );
            return;
        }

        if (user.isFriend(user1) && !user1.isFriend(user)) {
            user.sendMessage(
                    language.getMessage("friends.already_have_invited_friend")
            );
            return;
        }

        if (user1.isFriend(user) && !user.isFriend(user1)) {
            user.sendMessage(
                    language.getMessage("friends.already_have_an_friend_request")
            );
            return;
        }

        if (user.isSimilar(user1)) {
            user.sendMessage(
                    language.getMessage("friends.can\'t_invite_friend_to_you")
            );
            return;
        }

        user.addFriend(user1);

        user.sendMessage(
                String.format(
                        language.getMessage("friends.friend_request_send"),
                        user1.getDisplayName()
                )
        );

        JSONText jsonText = new JSONText()
                .text(
                        String.format(
                                "§aO usuário %s §adeseja tornar-se seu amigo.",
                                user.getPrefix() + user.getDisplayName()
                        )
                )
                .next()
                .text("\n")
                .next()
                .text("§aClique ")
                .next()
                .text("§lAQUI")
                .execute("/amigo aceitar " + user.getName())
                .next()
                .text("§r §apara aceitar o pedido de amizade.")
                .next();

        ProxyUser proxyUser = Proxy.getInstance().getProxyUserFactory().getUser(user1.getId());

        proxyUser.sendMessage(jsonText);
    }
}
