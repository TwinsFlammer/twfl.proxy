package com.redecommunity.proxy.announcement.listener;

import com.redecommunity.common.shared.databases.redis.handler.JedisMessageListener;
import com.redecommunity.common.shared.databases.redis.handler.annonation.ChannelName;
import com.redecommunity.common.shared.databases.redis.handler.event.JedisMessageEvent;
import com.redecommunity.proxy.announcement.data.Announcement;
import com.redecommunity.proxy.announcement.manager.AnnouncementManager;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = AnnouncementManager.CHANNEL_NAME)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        Integer announcementId = ((Long) jsonObject.get("announcement_id")).intValue();
        Boolean active = (Boolean) jsonObject.get("active");

        Announcement announcement = AnnouncementManager.getAnnouncement(announcementId);

        if (announcement == null) return;

        announcement.setActive(active);
    }
}
