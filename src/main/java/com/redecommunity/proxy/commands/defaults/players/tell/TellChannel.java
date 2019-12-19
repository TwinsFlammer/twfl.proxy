package com.redecommunity.proxy.commands.defaults.players.tell;

import com.redecommunity.common.shared.databases.redis.channel.data.Channel;
import com.redecommunity.proxy.commands.defaults.players.tell.manager.TellManager;

/**
 * Created by @SrGutyerrez
 */
public class TellChannel extends Channel {
    @Override
    public String getName() {
        return TellManager.CHANNEL_NAME;
    }
}
