package com.redefocus.proxy.listeners.general;

import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by @SrGutyerrez
 */
public class ProxiedPlayerChatListener implements Listener {
    @EventHandler
    public void onChat(ChatEvent event) {
        String message = event.getMessage();

        event.setMessage(
                message.replaceAll(
                        "\\s+",
                        ""
                )
        );
    }
}
