package com.redecommunity.proxy;

import com.redecommunity.common.shared.permissions.user.data.User;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;

/**
 * Created by @SrGutyerrez
 */
public class Proxy extends Plugin {
    private static Proxy instance;

    public Proxy() {
        Proxy.instance = this;
    }

    @Override
    public void onEnable() {
    }

    public static Proxy getInstance() {
        return Proxy.instance;
    }

    public static Collection<User> getUsers() {
        return null;
    }
}
