package br.com.twinsflammer.proxy.connection.listeners.channel;

import br.com.twinsflammer.common.shared.databases.redis.channel.data.Channel;
import br.com.twinsflammer.common.shared.util.Constants;

/**
 * Created by @SrGutyerrez
 */
public class ConnectChannel extends Channel {
    @Override
    public String getName() {
        return Constants.CONNECT_CHANNEL;
    }
}
