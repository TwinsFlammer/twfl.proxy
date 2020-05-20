package br.com.twinsflammer.proxy.commands.defaults.players.tell.channel;

import br.com.twinsflammer.proxy.commands.defaults.players.tell.manager.DirectMessageManager;
import br.com.twinsflammer.common.shared.databases.redis.channel.data.Channel;

/**
 * Created by @SrGutyerrez
 */
public class TellChannel extends Channel {
    @Override
    public String getName() {
        return DirectMessageManager.CHANNEL_NAME;
    }
}
