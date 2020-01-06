package com.redecommunity.proxy.authentication.listener;

import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;

/**
 * Created by @SrGutyerrez
 */
public class UnauthenticatedUserChatListener implements Listener {
    private final String[] allowedCommands = new String[] {
            "login",
            "logar",
            "register",
            "registrar"
    };

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

        User user = UserManager.getUser(proxiedPlayer.getUniqueId());
        Language language = user.getLanguage();

        if (user.isLogged()) return;

        String message = event.getMessage();

        if (event.isCommand()) {
            String command = message.contains(" ") ? message.split(" ")[0] : message;

            if (!Arrays.asList(this.allowedCommands).contains(command.substring(1))) event.setCancelled(true);
        } else event.setCancelled(true);

        if (event.isCancelled())
            user.sendMessage(
                    language.getMessage("authentication.chat_not_allowed_unauthenticated")
            );
    }
}
