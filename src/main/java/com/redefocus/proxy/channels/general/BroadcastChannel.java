package com.redefocus.proxy.channels.general;

import com.redefocus.common.shared.databases.redis.channel.data.Channel;
import com.redefocus.common.shared.util.Constants;

/**
 * Created by @SrGutyerrez
 */
public class BroadcastChannel extends Channel {
    @Override
    public String getName() {
        return Constants.BROADCAST_MESSAGE;
    }
}
