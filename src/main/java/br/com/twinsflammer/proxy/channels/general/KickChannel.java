package br.com.twinsflammer.proxy.channels.general;

import br.com.twinsflammer.common.shared.databases.redis.channel.data.Channel;
import br.com.twinsflammer.common.shared.util.Constants;

/**
 * Created by @SrGutyerrez
 */
public class KickChannel extends Channel {
    @Override
    public String getName() {
        return Constants.KICK_CHANNEL;
    }
}
