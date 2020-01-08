package com.redecommunity.proxy.listeners.general;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.UUID;

/**
 * Created by @SrGutyerrez
 */
public class PreLoginListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PreLoginEvent event) {
        PendingConnection pendingConnection = event.getConnection();

        UUID uniqueId = pendingConnection.getUniqueId();
        String playerName = pendingConnection.getName();

        System.out.println("UniqueID: " + uniqueId);
        System.out.println("PlayerName: " + playerName);
    }
}
