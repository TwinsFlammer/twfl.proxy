package com.redecommunity.proxy.connection.listeners.channel;

import com.redecommunity.common.shared.databases.redis.channel.data.Channel;
import com.redecommunity.common.shared.util.Constants;

/**
 * Created by @SrGutyerrez
 */
public class ConnectChannel extends Channel {
    @Override
    public String getName() {
        return Constants.CONNECT_CHANNEL;
    }
}
