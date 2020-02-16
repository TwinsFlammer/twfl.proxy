package com.redefocus.proxy.commands.defaults.players.tell.channel;

import com.redefocus.common.shared.databases.redis.channel.data.Channel;
import com.redefocus.proxy.commands.defaults.players.tell.manager.DirectMessageManager;

/**
 * Created by @SrGutyerrez
 */
public class TellChannel extends Channel {
    @Override
    public String getName() {
        return DirectMessageManager.CHANNEL_NAME;
    }
}
