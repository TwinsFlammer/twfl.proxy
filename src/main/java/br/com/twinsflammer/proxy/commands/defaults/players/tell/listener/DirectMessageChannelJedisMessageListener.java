package br.com.twinsflammer.proxy.commands.defaults.players.tell.listener;

import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.databases.redis.handler.annonation.ChannelName;
import br.com.twinsflammer.common.shared.databases.redis.handler.event.JedisMessageEvent;
import br.com.twinsflammer.common.shared.language.enums.Language;
import br.com.twinsflammer.common.shared.permissions.group.data.Group;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.proxy.commands.defaults.players.tell.manager.DirectMessageManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by @SrGutyerrez
 */
public class DirectMessageChannelJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = DirectMessageManager.CHANNEL_NAME)
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

        Group highestGroup = senderUser.getHighestGroup();

        String from = String.format(
                senderLanguage.getMessage("tell.formats.from"),
                highestGroup.getColor() + highestGroup.getPrefix() + senderUser.getDisplayName(),
                subject
        );

        Group highestGroup1 = targetUser.getHighestGroup();

        String to = String.format(
                targetLanguage.getMessage("tell.formats.to"),
                highestGroup1.getColor() + highestGroup1.getPrefix() + targetUser.getDisplayName(),
                subject
        );

        if (targetPlayer != null) {
            targetPlayer.sendMessage(
                    Helper.colorize(from)
            );
        }

        if (senderPlayer != null) {
            DirectMessageManager.setDirectMessageId(
                    senderId,
                    targetId
            );

            senderPlayer.sendMessage(
                    Helper.colorize(to)
            );
        }
    }
}
