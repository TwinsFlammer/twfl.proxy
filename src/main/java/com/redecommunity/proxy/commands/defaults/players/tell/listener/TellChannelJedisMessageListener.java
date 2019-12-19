package com.redecommunity.proxy.commands.defaults.players.tell.listener;

import com.redecommunity.common.shared.databases.redis.handler.JedisMessageListener;
import com.redecommunity.common.shared.databases.redis.handler.annonation.ChannelName;
import com.redecommunity.common.shared.databases.redis.handler.event.JedisMessageEvent;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.util.Helper;
import com.redecommunity.proxy.commands.defaults.players.tell.manager.TellManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by @SrGutyerrez
 */
public class TellChannelJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = TellManager.CHANNEL_NAME)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        Integer targetId = ((Long) jsonObject.get("target_id")).intValue();
        Integer senderId = ((Long) jsonObject.get("sender_id")).intValue();
        String subject = (String) jsonObject.get("subject");

        User targetUser = UserManager.getUser(targetId);
        User senderUser = UserManager.getUser(senderId);

        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetUser.getUniqueId());
        ProxiedPlayer senderPlayer = ProxyServer.getInstance().getPlayer(senderUser.getUniqueId());

        Language targetLanguage = targetUser.getLanguage();
        Language senderLanguage = senderUser.getLanguage();

        String from = String.format(
                targetLanguage.getMessage("tell.formats.from"),
                senderUser.getHighestGroup().getPrefix() + senderUser.getDisplayName(),
                subject
        );

        String to = String.format(
                senderLanguage.getMessage("tell.formats.to"),
                targetUser.getHighestGroup().getPrefix() + targetUser.getDisplayName(),
                subject
        );

        if (targetPlayer != null) {
            targetPlayer.sendMessage(
                    Helper.colorize(from)
            );
        }

        if (senderPlayer != null) {
            TellManager.setDirectMessageId(
                    senderId,
                    targetId
            );

            senderPlayer.sendMessage(
                    Helper.colorize(to)
            );
        }
    }
}
