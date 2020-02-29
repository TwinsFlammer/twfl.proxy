package com.redecommunity.proxy.channels.general;

import com.redecommunity.common.shared.databases.redis.channel.data.Channel;
import com.redecommunity.common.shared.util.Constants;

/**
 * Created by @SrGutyerrez
 */
public class KickChannel extends Channel {
    @Override
    public String getName() {
        return Constants.KICK_CHANNEL;
    }
}