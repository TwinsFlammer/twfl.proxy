package com.redefocus.proxy.listeners.general;

import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by @SrGutyerrez
 */
public class ProxiedPlayerChatListener implements Listener {
    @EventHandler
    public void onChat(ChatEvent event) {
        event.setMessage(
                StringUtils.replaceEach(
                        event.getMessage(),
                        new String[]{
                                "\\s+"
                        },
                        new String[]{
                                ""
                        }
                )
        );
    }
}
