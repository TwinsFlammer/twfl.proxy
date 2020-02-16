package com.redefocus.proxy.connection.listeners.channel;

import com.redefocus.common.shared.databases.redis.channel.data.Channel;
import com.redefocus.common.shared.util.Constants;

/**
 * Created by @SrGutyerrez
 */
public class ConnectChannel extends Channel {
    @Override
    public String getName() {
        return Constants.CONNECT_CHANNEL;
    }
}
