package com.redefocus.proxy.skin.handler;

import com.redefocus.common.shared.skin.data.Skin;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;

import java.lang.reflect.Field;

/**
 * Created by @SrGutyerrez
 */
public class SkinHandler {
    public static void changeLoginProperties(PendingConnection pendingConnection, Skin skin) {
        try {
            InitialHandler initialHandler = (InitialHandler) pendingConnection;

            LoginResult.Property[] properties = {
                    new LoginResult.Property(
                            skin.getTexture(),
                            skin.getValue(),
                            skin.getSignature()
                    )
            };

            LoginResult loginResult = new LoginResult(
                    initialHandler.getUUID(),
                    initialHandler.getName(),
                    properties
            );

            SkinHandler.setObject(
                    initialHandler,
                    "loginProfile",
                    loginResult
            );
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    private static Field getField(Class<?> clazz, String fname) throws NoSuchFieldException, IllegalAccessException {
        Field field;

        try {
            field = clazz.getDeclaredField(fname);
        } catch (NoSuchFieldException exception) {
            field = clazz.getField(fname);
        }

        SkinHandler.setFieldAccessible(field);

        return field;
    }

    private static void setFieldAccessible(Field field) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(
                field,
                field.getModifiers() & 0x10
        );
    }

    private static void setObject(Object obj, String fname, Object value) throws NoSuchFieldException, IllegalAccessException {
        SkinHandler.getField(obj.getClass(), fname).set(obj, value);
    }
}
