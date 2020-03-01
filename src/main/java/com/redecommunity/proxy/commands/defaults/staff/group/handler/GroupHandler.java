package com.redecommunity.proxy.commands.defaults.staff.group.handler;

import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.util.Constants;
import org.json.simple.JSONObject;

/**
 * Created by @SrGutyerrez
 */
public class GroupHandler {
    public static void update(User user, Group group, Integer serverId, Long duration, Boolean action) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("user_id", user.getId());
        jsonObject.put("group_id", group.getId());
        jsonObject.put("server_id", serverId);
        jsonObject.put("duration", duration);
        jsonObject.put("action", action);

        Common.getInstance().getDatabaseManager()
                .getRedisManager()
                .getDatabases()
                .values()
                .forEach(redis -> redis.sendMessage(
                        Constants.USER_GROUP_CHANNEL,
                        jsonObject.toString()
                ));
    }
}
