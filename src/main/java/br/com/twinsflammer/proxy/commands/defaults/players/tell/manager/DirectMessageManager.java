package br.com.twinsflammer.proxy.commands.defaults.players.tell.manager;

import com.google.common.collect.Maps;
import br.com.twinsflammer.common.shared.databases.redis.data.Redis;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.common.shared.util.Helper;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class DirectMessageManager {
    public static final String CHANNEL_NAME = "direct_message_channel";

    private static HashMap<Integer, Integer> directMessages = Maps.newHashMap();

    public static Integer getDirectMessageId(Integer id) {
        return DirectMessageManager.directMessages.get(id);
    }

    public static Integer setDirectMessageId(Integer userId, Integer targetId) {
        return DirectMessageManager.directMessages.put(userId, targetId);
    }

    public static void sendMessage(User user, User user1, String[] args, MessageType messageType) {
        Redis redis = user.getRedis();

        Language language = user.getLanguage();

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        if (!user1.isOnline()) {
            user.sendMessage(
                    language.getMessage("messages.player.player_offline")
            );
            return;
        }

        if (user.isSimilar(user1)) {
            user.sendMessage(
                    language.getMessage("tell.messages.cant_send_to_yourself")
            );
            return;
        }

        if (!user1.isEnabled(Preference.PRIVATE_MESSAGE) && !user.hasGroup(GroupNames.DIRECTOR)
                || user1.isFriend(user) && user.isFriend(user1)) {
            user.sendMessage(
                    language.getMessage("tell.messages.target_disabled_private_messages")
            );
            return;
        }

        if (!user1.isLogged()) {
            user.sendMessage(
                    language.getMessage("messages.player.player_not_logged")
            );
            return;
        }

        JSONObject jsonObject = new JSONObject();

        String[] arguments = (messageType == MessageType.DIRECT_MESSAGE ? Helper.removeFirst(args) : args);

        String message = Helper.toMessage(arguments);

        jsonObject.put("target_id", user1.getId());
        jsonObject.put("sender_id", user.getId());
        jsonObject.put("subject", message);

        redis.sendMessage(
                DirectMessageManager.CHANNEL_NAME,
                jsonObject.toString()
        );
    }

    public enum MessageType {
        DIRECT_MESSAGE,
        REPLY_MESSAGE;
    }
}
