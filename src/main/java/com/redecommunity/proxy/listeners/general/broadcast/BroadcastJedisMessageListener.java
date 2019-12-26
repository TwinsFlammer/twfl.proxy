package com.redecommunity.proxy.listeners.general.broadcast;

import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.databases.redis.handler.JedisMessageListener;
import com.redecommunity.common.shared.databases.redis.handler.annonation.ChannelName;
import com.redecommunity.common.shared.databases.redis.handler.event.JedisMessageEvent;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.group.manager.GroupManager;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Constants;
import net.md_5.bungee.api.ProxyServer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by @SrGutyerrez
 */
public class BroadcastJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = Constants.BROADCAST_MESSAGE)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        System.out.println(message);

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        System.out.println(jsonObject);

        Integer groupId = ((Long) jsonObject.get("group_id")).intValue();
        JSONText jsonText = (JSONText) jsonObject.get("message");

        Group group = GroupManager.getGroup(groupId);

        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            User user = UserManager.getUser(proxiedPlayer.getUniqueId());

            if (user.hasGroup(group)) jsonText.send(user);
        });
    }
}
