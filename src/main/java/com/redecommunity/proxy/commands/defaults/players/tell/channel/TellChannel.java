package com.redecommunity.proxy.commands.defaults.players.tell.channel;

import com.redecommunity.common.shared.databases.redis.channel.data.Channel;
import com.redecommunity.proxy.commands.defaults.players.tell.manager.DirectMessageManager;

/**
 * Created by @SrGutyerrez
 */
public class TellChannel extends Channel {
    @Override
    public String getName() {
        return DirectMessageManager.CHANNEL_NAME;
    }
}
