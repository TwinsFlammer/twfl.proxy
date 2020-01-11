package com.redecommunity.proxy.listeners.general;

import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.skin.data.Skin;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Field;

/**
 * Created by @SrGutyerrez
 */
public class LoginListener implements Listener {
    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        PendingConnection pendingConnection = proxiedPlayer.getPendingConnection();

        User user = UserManager.getUser(pendingConnection.getName());

        if (user == null) return;

        Skin skin = user.getSkin();

        if (skin == null) return;

        try {
            InitialHandler initialHandler = (InitialHandler) pendingConnection;

            LoginResult.Property[] properties = {
                    new LoginResult.Property(
                            skin.getTexture(),
                            skin.getSignature(),
                            skin.getValue()
                    )
            };

            LoginResult loginResult = new LoginResult(
                    initialHandler.getUUID(),
                    initialHandler.getName(),
                    properties
            );

            this.setObject(
                    initialHandler,
                    "loginProfile",
                    loginResult
            );
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    private Field getField(Class<?> clazz, String fname) throws NoSuchFieldException, IllegalAccessException {
        Field field;

        try {
            field = clazz.getDeclaredField(fname);
        } catch (NoSuchFieldException exception) {
            field = clazz.getField(fname);
        }

        this.setFieldAccessible(field);

        return field;
    }

    private void setFieldAccessible(Field field) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & 0x10);
    }

    private void setObject(Object obj, String fname, Object value) throws NoSuchFieldException, IllegalAccessException {
        this.getField(obj.getClass(), fname).set(obj, value);
    }
}
