package com.redecommunity.proxy;

import com.redecommunity.api.bungeecord.CommunityPlugin;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.punish.manager.StartManager;

import java.util.Collection;

/**
 * Created by @SrGutyerrez
 */
public class Proxy extends CommunityPlugin {
    private static Proxy instance;

    public Proxy() {
        Proxy.instance = this;
    }

    @Override
    public void onEnablePlugin() {
        new StartManager();
    }

    @Override
    public void onDisablePlugin() {

    }

    public static Proxy getInstance() {
        return Proxy.instance;
    }

    public static Collection<User> getUsers() {
        return null;
    }
}
