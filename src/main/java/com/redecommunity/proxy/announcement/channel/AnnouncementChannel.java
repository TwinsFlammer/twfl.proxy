package com.redecommunity.proxy.announcement.channel;

import com.redecommunity.common.shared.databases.redis.channel.data.Channel;
import com.redecommunity.proxy.announcement.manager.AnnouncementManager;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementChannel extends Channel {
    @Override
    public String getName() {
        return AnnouncementManager.CHANNEL_NAME;
    }
}
