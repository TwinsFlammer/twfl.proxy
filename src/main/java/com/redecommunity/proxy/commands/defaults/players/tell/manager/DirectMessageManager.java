package com.redecommunity.proxy.commands.defaults.players.tell.manager;

import com.google.common.collect.Maps;
import com.redecommunity.common.shared.databases.redis.data.Redis;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by @SrGutyerrez
 */
public class DirectMessageManager {
    public static final String CHANNEL_NAME = "direct_message_channel";

    private static HashMap<Integer, Integer> directMessages = Maps.newHashMap();

    public static Integer getDirectMessageId(Integer id) {
        return DirectMessageManager.directMessages
                .entrySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getValue().equals(id))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public static Integer setDirectMessageId(Integer userId, Integer targetId) {
        return DirectMessageManager.directMessages.put(userId, targetId);
    }

    public static void sendMessage(User user, User user1, String[] args) {
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

        JSONObject jsonObject = new JSONObject();

        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++)
            message.append(args[i])
                    .append(" ");

        jsonObject.put("target_id", user1.getId());
        jsonObject.put("sender_id", user.getId());
        jsonObject.put("subject", message.toString());

        redis.sendMessage(
                DirectMessageManager.CHANNEL_NAME,
                jsonObject.toString()
        );
    }
}
