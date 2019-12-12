package com.redecommunity.proxy;

import net.md_5.bungee.api.plugin.Plugin;

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
}
